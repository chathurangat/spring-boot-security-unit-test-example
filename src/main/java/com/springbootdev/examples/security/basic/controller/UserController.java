package com.springbootdev.examples.security.basic.controller;

import com.springbootdev.examples.security.basic.exception.InvalidUserIdException;
import com.springbootdev.examples.security.basic.exception.ModelNotFoundException;
import com.springbootdev.examples.security.basic.exception.PersistentException;
import com.springbootdev.examples.security.basic.exception.ValidationException;
import com.springbootdev.examples.security.basic.model.dto.request.AddUserRequest;
import com.springbootdev.examples.security.basic.model.dto.response.AddUserResponse;
import com.springbootdev.examples.security.basic.model.dto.response.FindUserResponse;
import com.springbootdev.examples.security.basic.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(value = "user-controller", description = "Endpoints for handling and managing user related operations", tags = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "creating new user")
    @PostMapping("/users")
    public AddUserResponse createUser(@Valid @RequestBody AddUserRequest addUserRequest, BindingResult bindingResult) throws PersistentException, ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        return userService.create(addUserRequest);
    }


    @ApiOperation(value = "finding the user by id")
    @GetMapping("/users/{id}")
    public FindUserResponse findUserById(@PathVariable("id") Integer id) throws ModelNotFoundException, InvalidUserIdException {
        if (id > 0) {
            return userService.findUserById(id);
        }
        throw new InvalidUserIdException("Invalid  user Id");
    }
}
