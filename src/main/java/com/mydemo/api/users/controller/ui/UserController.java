package com.mydemo.api.users.controller.ui;

import com.mydemo.api.users.controller.model.CreateUserRequestModel;
import com.mydemo.api.users.controller.model.CreateUserResponseModel;
import com.mydemo.api.users.service.UserService;
import com.mydemo.api.users.shared.UserDto;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController
{
    @Autowired
    private Environment env;
    @Autowired
    private UserService userService;

    //GET status http://localhost:8080/users/status
    @GetMapping("/status")
    public String status()
    {
        return "User Service is working on Port: "+ env.getProperty("local.server.port");
    }

    @PostMapping
    public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel userDetails)
    {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto= modelMapper.map(userDetails, UserDto.class);
        UserDto createdUser= userService.createUser(userDto);

        CreateUserResponseModel returnValue= modelMapper.map(createdUser, CreateUserResponseModel.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
    }
}
