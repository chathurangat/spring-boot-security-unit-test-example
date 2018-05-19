package com.springbootdev.examples.security.basic.service;

import com.springbootdev.examples.security.basic.exception.ModelNotFoundException;
import com.springbootdev.examples.security.basic.exception.PersistentException;
import com.springbootdev.examples.security.basic.model.dto.request.AddUserRequest;
import com.springbootdev.examples.security.basic.model.dto.response.AddUserResponse;
import com.springbootdev.examples.security.basic.model.dto.response.FindUserResponse;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Service
public class UserService implements GenericService<AddUserRequest, AddUserResponse> {

    @Override
    @RolesAllowed("ROLE_ADMIN")
    public AddUserResponse create(AddUserRequest addUserRequest) throws PersistentException {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        AddUserResponse addUserResponse = new AddUserResponse();
        addUserResponse.setUserId(1);
        addUserResponse.setUsername(addUserRequest.getUsername());
        addUserResponse.setCreatedOn(sdf.format(cal.getTime()));
        return addUserResponse;
    }


    @RolesAllowed("ROLE_USER")
    public FindUserResponse findUserById(Integer id) throws ModelNotFoundException {
        if (id > 100) {
            throw new ModelNotFoundException("Valid user id is required ");
        }
        return FindUserResponse.builder()
                .userId(id)
                .name("Chathuranga Tennakoon")
                .username("chathuranga")
                .build();
    }
}
