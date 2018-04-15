package me.arminb.sara;

import me.arminb.sara.configuration.MainConfiguration;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = MainConfiguration.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CommentIntegrationTest {

    private static Logger logger = LoggerFactory.getLogger(UserIntegrationTest.class);
    private static String questionId;
    private static String userId;
    private static String answerId;
    private static String commentId;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }


    // Create an answer
    private static String createUserInJson(String username, String email, String password) {
        return "{ \"username\": \"" + username + "\"," +
                "\"email\": \"" + email + "\"," +
                "\"password\": \"" + password + "\"}";
    }

    private static String createQuestionInJson(String title, String content, String user, String[] tags, String rate) {
        String tagStr = "[";
        for (int i = 0; i < tags.length; i++) {
            if (i == tags.length - 1) {
                tagStr = tagStr + "\"" + tags[i] + "\"";
            } else {
                tagStr = tagStr + "\"" + tags[i] + "\", ";
            }
        }
        tagStr = tagStr + "]";
        String jsonString = "{ \"title\": \"" + title + "\"," +
                "\"content\": \"" + content + "\"," +
                "\"userId\": \"" + user + "\"," +
                "\"tags\": " + tagStr;

        if (!rate.isEmpty()) {
            jsonString += ",\"rate\": \"" + rate + "\"}";
        } else {
            jsonString += "}";
        }

        return jsonString;
    }

    private static String createAnswerInJson(String answer, String user, String rate) {

        String jsonString = "{ \"answer\": \"" + answer + "\"," +
                "\"userId\": \"" + user + "\"";

        if (!rate.isEmpty()) {
            jsonString += ",\"rate\": \"" + rate + "\"}";
        } else {
            jsonString += "}";
        }
        return jsonString;
    }

    @Test
    public void t0_addUser() throws Exception {

        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createUserInJson("testuser-comment", "testuser-comment@gmail.com", "1234")));

        String response = resultActions.andReturn().getResponse().getContentAsString();
        userId = response.split(":")[1].split(",")[0];
        userId = userId.substring(1, userId.length() - 1);

        resultActions.andExpect(status().isOk());

    }

    @Test
    public void t1_addQuestion() throws Exception {

        String[] tags = {"spring", "java"};

        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createQuestionInJson("comment:How to learn Spring Framework?", "You want to learn the basics with Hands-on examples",
                        userId, tags, "")));
        String response = resultActions.andReturn().getResponse().getContentAsString();
        if (!response.isEmpty()) {
            questionId = response.split(":")[1].split(",")[0];
            questionId = questionId.substring(1, questionId.length() - 1);
        }
        resultActions.andExpect(status().isOk());

    }

    @Test
    public void t2_addAnswer() throws Exception {

        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/questions/{qid}/answers", questionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createAnswerInJson("This is answer 1", userId, "")));
        String response = resultActions.andReturn().getResponse().getContentAsString();
        if (!response.isEmpty()) {
            answerId = response.split(":")[1].split(",")[0];
            answerId = answerId.substring(1, answerId.length() - 1);
        }
        resultActions.andExpect(status().isOk());

    }

    // CRUD on Comment
    private static String createCommentInJson(String content, String user) {
        String jsonString = "{\"content\": \"" + content+"\"";

        if (!user.isEmpty()) {
            jsonString += ",\"user_id\": \"" + user + "\"}";
        } else {
            jsonString += "}";
        }
        System.out.println(jsonString);
        return jsonString;
    }

    @Test
    public void t3_addComment() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/answers/{aid}/comments", answerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createCommentInJson("This is comment 1", userId)));
        String response = resultActions.andReturn().getResponse().getContentAsString();
        if (!response.isEmpty()) {
            commentId = response.split(":")[1].split(",")[0];
            commentId = commentId.substring(1, commentId.length() - 1);
        }
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void t4_updateComment() throws Exception {
        ResultMatcher ok = MockMvcResultMatchers.status().isOk();

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/answers/{aid}/comments/{cid}", answerId, commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createCommentInJson("This is update for comment 1", ""));
        this.mockMvc.perform(builder).andExpect(ok);

    }

    @Test
    public void t5_deleteComment() throws Exception {

        ResultMatcher ok = MockMvcResultMatchers.status().isOk();

        if (commentId != null) {
            MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete("/comments/{cid}", commentId);
            this.mockMvc.perform(builder).andExpect(ok);
        }
    }

    @Test
    public void t6_deleteCommentNotFound() throws Exception {
        ResultMatcher ok = MockMvcResultMatchers.status().isOk();
        ResultMatcher notFound = MockMvcResultMatchers.status().isNotFound();

        if (commentId != null) {
            MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete("/comments/{cid}", commentId);
            this.mockMvc.perform(builder).andExpect(notFound);
        }
    }


    @Test
    public void t7_deleteAnswer() throws Exception {

        ResultMatcher ok = MockMvcResultMatchers.status().isOk();

        if (answerId != null) {
            MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete("/answers/{aid}", answerId);
            this.mockMvc.perform(builder).andExpect(ok);
        }
    }

    @Test
    public void t8_deleteQuestion() throws Exception {

        ResultMatcher ok = MockMvcResultMatchers.status().isOk();

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/questions");
        this.mockMvc.perform(builder).andExpect(ok);

        if (questionId != null) {
            builder = MockMvcRequestBuilders.delete("/questions/{id}", questionId);
        }
        this.mockMvc.perform(builder).andExpect(ok);
    }

    @Test
    public void t9_deleteUser() throws Exception {

        ResultMatcher ok = MockMvcResultMatchers.status().isOk();

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/users");
        this.mockMvc.perform(builder).andExpect(ok);

        if (userId != null) {
            builder = MockMvcRequestBuilders.delete("/users/{id}", userId);
        }
        this.mockMvc.perform(builder).andExpect(ok);
    }


}
