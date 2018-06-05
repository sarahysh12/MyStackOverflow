package me.arminb.sara;

import me.arminb.sara.configuration.MainConfiguration;
import me.arminb.sara.controller.UserController;
import org.bson.types.ObjectId;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = MainConfiguration.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserIntegrationTest {

    private static Logger logger = LoggerFactory.getLogger(UserIntegrationTest.class);
    private static String userId;

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


    @Test
    public void t1_addUser() throws Exception {

        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createUserInJson("testuser","testuser@gmail.com", "1234")));

        String response = resultActions.andReturn().getResponse().getContentAsString();
        userId = response.split(":")[1].split(",")[0];
        userId = userId.substring(1,userId.length()-1);

        resultActions.andExpect(status().isOk());

    }

    @Test
    public void t2_findRecentAddedUser() throws Exception {

        ResultMatcher ok = MockMvcResultMatchers.status().isOk();

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/users");
        this.mockMvc.perform(builder).andExpect(ok);


        builder = MockMvcRequestBuilders.get("/users?username=testuser");
        this.mockMvc.perform(builder).andExpect(ok);

        builder = MockMvcRequestBuilders.get("/users?email=testuser@gmail.com");
        this.mockMvc.perform(builder).andExpect(ok);

    }

    @Test
    public void t3_findUserById() throws Exception{

        ResultMatcher ok = MockMvcResultMatchers.status().isOk();

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/users");
        this.mockMvc.perform(builder).andExpect(ok);

        if(userId != null) {
            builder = MockMvcRequestBuilders.get("/users/{id}", userId);
        }
        this.mockMvc.perform(builder).andExpect(ok);
    }

    @Test
    public void t4_updateUser() throws Exception {

        ResultMatcher ok = MockMvcResultMatchers.status().isOk();

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createUserInJson("testuser_update","testuser_update@gmail.com", "1234"));

        this.mockMvc.perform(builder).andExpect(ok);

    }

    @Test
    public void t5_deleteUser() throws Exception {

        ResultMatcher ok = MockMvcResultMatchers.status().isOk();

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/users");
        this.mockMvc.perform(builder).andExpect(ok);

        if (userId != null) {
            builder = MockMvcRequestBuilders.delete("/users/{id}",userId);
        }
        this.mockMvc.perform(builder).andExpect(ok);
    }

    @Test
    public void t6_findUserByIdNotFound() throws Exception{

        ResultMatcher notFound = MockMvcResultMatchers.status().isNotFound();
        ResultMatcher ok = MockMvcResultMatchers.status().isOk();

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/users");
        this.mockMvc.perform(builder).andExpect(ok);

        if(userId != null) {
            builder = MockMvcRequestBuilders.get("/users/" + userId);
        }
        this.mockMvc.perform(builder).andExpect(notFound);
    }

    @Test
    public void t7_deleteUserNotFound() throws Exception {

        ResultMatcher ok = MockMvcResultMatchers.status().isOk();
        ResultMatcher notFound = MockMvcResultMatchers.status().isNotFound();

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/users");
        this.mockMvc.perform(builder).andExpect(ok);

        if (userId != null) {
            builder = MockMvcRequestBuilders.delete("/users/{id}",userId);
        }
        this.mockMvc.perform(builder).andExpect(notFound);
    }


    //TODO: delete by id and id is null (it passes now)
    //TODO: findall
}