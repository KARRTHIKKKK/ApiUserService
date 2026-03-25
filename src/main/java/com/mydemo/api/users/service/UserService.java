package com.mydemo.api.users.service;

import com.mydemo.api.users.shared.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService
{
    UserDto createUser(UserDto userDetails);
}
