package com.mydemo.api.users.security;


import java.io.IOException;
import java.util.ArrayList;
import com.mydemo.api.users.controller.model.LoginRequestModel;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter
{
    public AuthenticationFilter(AuthenticationManager authenticationManager)
    {
        super(authenticationManager);
    }

    @Override
    //accepts http servlet request and response object
    public Authentication
    attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        try
        {
            LoginRequestModel creds = new ObjectMapper()
                    .readValue(req.getInputStream(), LoginRequestModel.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>()));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
