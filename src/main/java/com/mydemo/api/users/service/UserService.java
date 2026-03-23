package com.mydemo.api.users.service;

import com.mydemo.api.users.shared.UserDto;

public interface UserService
{
    UserDto createUser(UserDto userDetails);
}
