package com.mydemo.api.users.service.impl;

import com.mydemo.api.users.data.*;
import com.mydemo.api.users.service.UserService;
import com.mydemo.api.users.shared.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService
{
    UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto createUser(UserDto userDetails)
    {
        userDetails.setUserId(UUID.randomUUID().toString());
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserEntity userEntiry= modelMapper.map(userDetails, UserEntity.class);

        userEntiry.setEncryptedPassword("test");
        userRepository.save(userEntiry);

        UserDto returnValue= modelMapper.map(userEntiry, UserDto.class);
        return returnValue;
    }
}
