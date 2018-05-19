package com.springbootdev.examples.security.basic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbootdev.examples.security.basic.config.SpringSecurityConfig;
import com.springbootdev.examples.security.basic.model.dto.request.AddUserRequest;
import com.springbootdev.examples.security.basic.model.dto.response.AddUserResponse;
import com.springbootdev.examples.security.basic.model.dto.response.FindUserResponse;
import com.springbootdev.examples.security.basic.service.UserService;

import static org.junit.Assert.*;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;


@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@Import(SpringSecurityConfig.class)
public class UserControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;


    /*
      trying to create user with valid credentials and without a request body.
      Therefore Bad Request error is expected with 400 status code
    */
    @Test
    public void testCreateUser1() throws Exception {

        String username = "chathuranga";
        String password = "123";
        Integer userId = 1;
        String name = "Chathuranga T";
        String date = "2017-10-10";

        //building the mock response
        AddUserResponse addUserResponse = AddUserResponse.builder()
                .userId(userId)
                .username(username)
                .createdOn(date)
                .build();

        //mocking the bean for any object of AddUserRequest.class
        Mockito.when(userService.create(ArgumentMatchers.any(AddUserRequest.class))).thenReturn(addUserResponse);

        //here no request body is added. Therefore the backend server should throw the BadRequest Exception
        mockMvc.perform(post("/users")
                .with(httpBasic(username, password)))
                .andExpect(status().isBadRequest());
    }


    /*
      trying to create user with valid credentials and proper request body.
    */
    @Test
    public void testCreateUser2() throws Exception {
        String username = "chathuranga";
        String password = "123";
        Integer userId = 1;
        String name = "Chathuranga T";
        String date = "2017-10-10";

        //building the request object
        AddUserRequest addUserRequest = AddUserRequest.builder()
                .name(name)
                .username(username)
                .password(password)
                .build();

        //building the mock response
        AddUserResponse addUserResponse = AddUserResponse.builder()
                .userId(userId)
                .username(username)
                .createdOn(date)
                .build();

        //mocking the bean for any object of AddUserRequest.class
        Mockito.when(userService.create(ArgumentMatchers.any(AddUserRequest.class))).thenReturn(addUserResponse);

        //response is retrieved as MvcResult
        MvcResult mvcResult = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addUserRequest))
                .with(httpBasic(username, password)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.user_id", CoreMatchers.is(userId)))
                .andExpect(jsonPath("$.username", CoreMatchers.is(username)))
                .andExpect(jsonPath("$.created_on", CoreMatchers.is(date)))
                .andReturn();


        //json response body is converted/mapped to the Java Object
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        AddUserResponse userCreated = new ObjectMapper().readValue(jsonResponse, AddUserResponse.class);

        assertNotNull(userCreated);
        assertEquals(userCreated.getUserId(), userId);
        assertEquals(userCreated.getUsername(), username);
        assertEquals(userCreated.getCreatedOn(), date);
    }


    /*
      trying to create user with invalid credentials and proper request body.
      (401 unauthorized)
    */
    @Test
    public void testCreateUser3() throws Exception {

        String username = "invalid_username";
        String password = "invalid_password";
        Integer userId = 1;
        String name = "Chathuranga T";
        String date = "2017-10-10";

        //building the request object
        AddUserRequest addUserRequest = AddUserRequest.builder()
                .name(name)
                .username(username)
                .password(password)
                .build();

        //building the mock response
        AddUserResponse addUserResponse = AddUserResponse.builder()
                .userId(userId)
                .username(username)
                .createdOn(date)
                .build();

        //mocking the bean for any object of AddUserRequest.class
        Mockito.when(userService.create(ArgumentMatchers.any(AddUserRequest.class))).thenReturn(addUserResponse);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addUserRequest))
                .with(httpBasic(username, password))
        ).andExpect(status().isUnauthorized());
    }


    /*
     trying to create user with valid credentials. but with invalid request body
     (with empty fields)
    */
    @Test
    public void testCreateUser4() throws Exception {

        String username = "chathuranga";
        String password = "123";
        Integer userId = 1;
        String name = "Chathuranga T";
        String date = "2017-10-10";

        //building the request object (object with empty attributes)
        AddUserRequest addUserRequest = AddUserRequest.builder().build();

        //building the mock response
        AddUserResponse addUserResponse = AddUserResponse.builder()
                .userId(userId)
                .username(username)
                .createdOn(date)
                .build();

        //mocking the bean for any object of AddUserRequest.class
        Mockito.when(userService.create(ArgumentMatchers.any(AddUserRequest.class))).thenReturn(addUserResponse);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addUserRequest))
                .with(httpBasic(username, password)))
                .andExpect(status().isUnprocessableEntity());
    }


    /*
     trying to get the user by providing valid user id
    */
    @Test
    public void testFindUserById1() throws Exception {

        String username = "chathuranga";
        String password = "123";
        Integer userId = 1;
        String name = "Chathuranga T";

        //building the mock response
        FindUserResponse findUserResponse = FindUserResponse.builder()
                .userId(userId)
                .name(name)
                .username(username)
                .build();

        //mocking the bean
        Mockito.when(userService.findUserById(userId)).thenReturn(findUserResponse);


        //response is retrieved as MvcResult
        mockMvc.perform(get("/users/{id}", userId)
                .accept(MediaType.APPLICATION_JSON)
                .with(httpBasic(username, password)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.user_id", CoreMatchers.is(userId)))
                .andExpect(jsonPath("$.name", CoreMatchers.is(name)))
                .andExpect(jsonPath("$.username", CoreMatchers.is(username)));
    }


    /*
     trying to get the user by providing invalid user Id
    */
    @Test
    public void testFindUserById2() throws Exception {

        String username = "chathuranga";
        String password = "123";
        Integer userId = -1;
        String name = "Chathuranga T";

        //building the mock response
        FindUserResponse findUserResponse = FindUserResponse.builder()
                .userId(userId)
                .name(name)
                .username(username)
                .build();

        //mocking the bean
        Mockito.when(userService.findUserById(userId)).thenReturn(findUserResponse);

        //response is retrieved as MvcResult
        mockMvc.perform(get("/users/{id}", userId)
                .accept(MediaType.APPLICATION_JSON)
                .with(httpBasic(username, password)))
                .andExpect(status().isUnprocessableEntity());
    }
}
