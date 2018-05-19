package com.springbootdev.examples.security.basic.controller;

import static org.junit.Assert.*;

import com.springbootdev.examples.security.basic.model.dto.request.AddUserRequest;
import com.springbootdev.examples.security.basic.model.dto.response.AddUserResponse;
import com.springbootdev.examples.security.basic.model.dto.response.FindUserResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerWebServerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;


    @Test
    public void testT() {
        assertTrue(true);
    }


    /*
       trying to create user with valid credentials and without a request body.
       Therefore Bad Request error is expected with 400 status code
     */
    @Test
    public void testCreateUser1() {
        String url = "http://localhost:" + port + "/users";

        String username = "chathuranga";
        String password = "123";

        String authorizationHeader = "Basic " + DatatypeConverter.printBase64Binary((username + ":" + password).getBytes());

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.add("Authorization", authorizationHeader);

        HttpEntity<Object> requestEntity = new HttpEntity<>(requestHeaders);


        ResponseEntity<AddUserResponse> responseEntity = testRestTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                AddUserResponse.class
        );

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }


    /*
      trying to create user with valid credentials and proper request body.
    */
    @Test
    public void testCreateUser2() {
        String url = "http://localhost:" + port + "/users";

        String username = "chathuranga";
        String password = "123";

        String authorizationHeader = "Basic " + DatatypeConverter.printBase64Binary((username + ":" + password).getBytes());

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.add("Authorization", authorizationHeader);


        AddUserRequest addUserRequest = new AddUserRequest();
        addUserRequest.setName("Sample User");
        addUserRequest.setUsername("user1");
        addUserRequest.setPassword("pass123");

        HttpEntity<AddUserRequest> requestEntity = new HttpEntity<>(addUserRequest, requestHeaders);

        ResponseEntity<AddUserResponse> responseEntity = testRestTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                AddUserResponse.class
        );

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        AddUserResponse addUserResponse = responseEntity.getBody();
        assertNotNull(addUserResponse);

        assertNotNull(addUserResponse.getCreatedOn());
        assertEquals(new Integer(1), addUserResponse.getUserId());
        assertEquals("user1", addUserResponse.getUsername());
    }


    /*
      trying to create user with invalid credentials and proper request body.
    */
    @Test
    public void testCreateUser3() {
        String url = "http://localhost:" + port + "/users";

        String username = "chathuranga123";
        String password = "123";

        String authorizationHeader = "Basic " + DatatypeConverter.printBase64Binary((username + ":" + password).getBytes());

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.add("Authorization", authorizationHeader);


        AddUserRequest addUserRequest = new AddUserRequest();
        addUserRequest.setName("Sample User");
        addUserRequest.setUsername("user1");
        addUserRequest.setPassword("pass123");

        HttpEntity<AddUserRequest> requestEntity = new HttpEntity<>(addUserRequest, requestHeaders);

        ResponseEntity<AddUserResponse> responseEntity = testRestTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                AddUserResponse.class
        );

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }


    /*
       trying to create user with valid credentials. but with invalid request body
       (with empty fields)
     */
    @Test
    public void testCreateUser4() {
        String url = "http://localhost:" + port + "/users";

        String username = "chathuranga";
        String password = "123";

        String authorizationHeader = "Basic " + DatatypeConverter.printBase64Binary((username + ":" + password).getBytes());

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.add("Authorization", authorizationHeader);

        //fields are empty
        AddUserRequest addUserRequest = new AddUserRequest();

        HttpEntity<AddUserRequest> requestEntity = new HttpEntity<>(addUserRequest, requestHeaders);

        ResponseEntity<AddUserResponse> responseEntity = testRestTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                AddUserResponse.class
        );

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }


    /*
      trying to create user with valid credentials and proper request body.
      here the basic authentication is made with inbuilt method available in the TestRestTemplate.
    */
    @Test
    public void testCreateUser5() {
        String url = "http://localhost:" + port + "/users";

        String username = "chathuranga";
        String password = "123";

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));


        AddUserRequest addUserRequest = new AddUserRequest();
        addUserRequest.setName("Sample User");
        addUserRequest.setUsername("user1");
        addUserRequest.setPassword("pass123");

        HttpEntity<AddUserRequest> requestEntity = new HttpEntity<>(addUserRequest, requestHeaders);

        //basic authentication is made with  'withBasicAuth' method available in the TestRestTemplate
        ResponseEntity<AddUserResponse> responseEntity = testRestTemplate.withBasicAuth(username, password)
                .exchange(
                        url,
                        HttpMethod.POST,
                        requestEntity,
                        AddUserResponse.class
                );

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        AddUserResponse addUserResponse = responseEntity.getBody();
        assertNotNull(addUserResponse);

        assertNotNull(addUserResponse.getCreatedOn());
        assertEquals(new Integer(1), addUserResponse.getUserId());
        assertEquals("user1", addUserResponse.getUsername());
    }


    /*
       trying to get the user by providing valid user id
     */
    @Test
    public void testFindUserById1() {

        String username = "chathuranga";
        String password = "123";
        Integer userId = 1;

        String url = "http://localhost:" + port + "/users/" + userId;

        String authorizationHeader = "Basic " + DatatypeConverter.printBase64Binary((username + ":" + password).getBytes());

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.add("Authorization", authorizationHeader);

        //fields are empty
        AddUserRequest addUserRequest = new AddUserRequest();

        HttpEntity<AddUserRequest> requestEntity = new HttpEntity<>(addUserRequest, requestHeaders);

        ResponseEntity<FindUserResponse> responseEntity = testRestTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                FindUserResponse.class
        );

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }


    /*
     trying to get the user by providing invalid user Id
     */
    @Test
    public void testFindUserById2() {

        String username = "chathuranga";
        String password = "123";
        //invalid user id
        Integer userId = -1;

        String url = "http://localhost:" + port + "/users/" + userId;

        String authorizationHeader = "Basic " + DatatypeConverter.printBase64Binary((username + ":" + password).getBytes());

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.add("Authorization", authorizationHeader);

        HttpEntity<AddUserRequest> requestEntity = new HttpEntity<>(requestHeaders);

        ResponseEntity<FindUserResponse> responseEntity = testRestTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                FindUserResponse.class
        );

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }


    /*
     trying to get the user by providing user Id which is greater than 100 (identified as a non existing user)
    */
    @Test
    public void testFindUserById3() {

        String username = "chathuranga";
        String password = "123";
        //invalid user id (backend service treated the userId which is greater than 100 as invalid userId)
        Integer userId = 200;

        String url = "http://localhost:" + port + "/users/" + userId;

        String authorizationHeader = "Basic " + DatatypeConverter.printBase64Binary((username + ":" + password).getBytes());

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.add("Authorization", authorizationHeader);

        HttpEntity<AddUserRequest> requestEntity = new HttpEntity<>(requestHeaders);

        ResponseEntity<FindUserResponse> responseEntity = testRestTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                FindUserResponse.class
        );

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }


    /*
      trying to invoke secured REST endpoint with authenticated testRestTemplate
    */
    @Test
    public void testFindUserById4() {

        String username = "chathuranga";
        String password = "123";
        //invalid user id (backend service treated the userId which is greater than 100 as invalid userId)
        Integer userId = 200;

        String url = "http://localhost:" + port + "/users/" + userId;

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<AddUserRequest> requestEntity = new HttpEntity<>(requestHeaders);

        TestRestTemplate testRestTemplate1 = new TestRestTemplate(username, password, TestRestTemplate.HttpClientOption.ENABLE_COOKIES);
        ResponseEntity<FindUserResponse> responseEntity = testRestTemplate1.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                FindUserResponse.class
        );

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}
