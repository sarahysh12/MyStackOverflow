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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = MainConfiguration.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class QuestionIntegrationTest {

    private static Logger logger = LoggerFactory.getLogger(UserIntegrationTest.class);
    public static String questionId;
    public static String userId;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    private static String createUserInJson (String username, String email, String password) {
        return "{ \"username\": \"" + username + "\"," +
                "\"email\": \"" + email + "\"," +
                "\"password\": \"" + password + "\"}";
    }


    private static String createQuestionInJson (String title, String content, String user, String[] tags, String rate) {
        String tagStr = "[";
        for (int i = 0; i < tags.length; i++) {
            if (i == tags.length-1){
                tagStr = tagStr+ "\""+ tags[i]+"\"";
            }
            else {
                tagStr = tagStr + "\"" + tags[i] + "\", ";
            }
        }
        tagStr = tagStr+"]";
        String jsonString =  "{ \"title\": \"" + title + "\"," +
                "\"content\": \"" + content + "\"," +
                "\"userId\": \"" + user + "\"," +
                "\"tags\": " + tagStr;

        if (!rate.isEmpty()){
            jsonString += ",\"rate\": \"" + rate + "\"}";
        }else {
            jsonString += "}";
        }

        return jsonString;
    }


    @Test
    public void t1_addUser() throws Exception {

        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createUserInJson("testuser-question","testuser-question@gmail.com", "1234")));

        String response = resultActions.andReturn().getResponse().getContentAsString();
        userId = response.split(":")[1].split(",")[0];
        userId = userId.substring(1,userId.length()-1);

        resultActions.andExpect(status().isOk());

    }

    @Test
    public void t2_addQuestion() throws Exception {

        String[] tags = {"spring", "java"};

        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createQuestionInJson("How to learn Spring Framework?","You want to learn the basics with Hands-on examples",
                        userId, tags, "")));
        String response = resultActions.andReturn().getResponse().getContentAsString();
        if (!response.isEmpty()) {
            questionId = response.split(":")[1].split(",")[0];
            questionId = questionId.substring(1, questionId.length() - 1);
        }
        resultActions.andExpect(status().isOk());

    }

    @Test
    public void t3_findRecentAddedQuestion() throws Exception {

        ResultMatcher ok = MockMvcResultMatchers.status().isOk();

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/questions");
        this.mockMvc.perform(builder).andExpect(ok);


        //builder = MockMvcRequestBuilders.get("/questions?title=How to learn Spring Framework?");
        //this.mockMvc.perform(builder).andExpect(ok);

        builder = MockMvcRequestBuilders.get("/questions?tag=spring");
        this.mockMvc.perform(builder).andExpect(ok);

    }

    @Test
    public void t4_findQuestionById() throws Exception{

        ResultMatcher ok = MockMvcResultMatchers.status().isOk();

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/questions");
        this.mockMvc.perform(builder).andExpect(ok);

        if(questionId != null) {
            builder = MockMvcRequestBuilders.get("/questions/{id}", questionId);
        }
        this.mockMvc.perform(builder).andExpect(ok);
    }

    @Test
    public void t5_updateQuestion() throws Exception {
        String[] tags = {"spring", "java", "junit"};
        ResultMatcher ok = MockMvcResultMatchers.status().isOk();

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/questions/{id}", questionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createQuestionInJson("How to learn Spring Framework quickly?","You want to learn the basics with Hands-on examples",
                        userId, tags, "1"));

        this.mockMvc.perform(builder).andExpect(ok);

    }

    @Test
    public void t6_deleteQuestion() throws Exception {

        ResultMatcher ok = MockMvcResultMatchers.status().isOk();

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/questions");
        this.mockMvc.perform(builder).andExpect(ok);

        if (questionId != null) {
            builder = MockMvcRequestBuilders.delete("/questions/{id}", questionId);
        }
        this.mockMvc.perform(builder).andExpect(ok);
    }

    @Test
    public void t7_deleteUser() throws Exception {

        ResultMatcher ok = MockMvcResultMatchers.status().isOk();

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/users");
        this.mockMvc.perform(builder).andExpect(ok);

        if (userId != null) {
            builder = MockMvcRequestBuilders.delete("/users/{id}",userId);
        }
        this.mockMvc.perform(builder).andExpect(ok);
    }

    @Test
    public void t8_findQuestionByIdNotFound() throws Exception{
        ResultMatcher notFound = MockMvcResultMatchers.status().isNotFound();
        ResultMatcher ok = MockMvcResultMatchers.status().isOk();

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/questions");
        this.mockMvc.perform(builder).andExpect(ok);

        if(questionId != null) {
            builder = MockMvcRequestBuilders.get("/questions/" + questionId);
        }
        this.mockMvc.perform(builder).andExpect(notFound);
    }

    @Test
    public void t9_deleteQuestionNotFound() throws Exception {
        ResultMatcher ok = MockMvcResultMatchers.status().isOk();
        ResultMatcher notFound = MockMvcResultMatchers.status().isNotFound();

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/questions");
        this.mockMvc.perform(builder).andExpect(ok);

        if (questionId != null) {
            builder = MockMvcRequestBuilders.delete("/questions/{id}",questionId);
        }
        this.mockMvc.perform(builder).andExpect(notFound);
    }


    //TODO: delete by id and id is null (it get passed now)
    //TODO: findall, parse response
    //TODO: page and pageCount
}
