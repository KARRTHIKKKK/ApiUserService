package com.mydemo.api.users.security;

import com.mydemo.api.users.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration  // Marks this class as a Spring configuration class
@EnableWebSecurity  // Enables Spring Security's web security support
public class WebSecurity
{
    private final Environment env;                             // To read application.properties values
    private final UserService usersService;                    // To load user details during authentication
    private final BCryptPasswordEncoder bCryptPasswordEncoder; // To encode/verify passwords

    // Constructor injection — Spring will auto-inject all 3 dependencies
    public WebSecurity(Environment env, UserService usersService, BCryptPasswordEncoder bCryptPasswordEncoder)
    {
        this.env = env;
        this.usersService = usersService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Bean  // Registers this SecurityFilterChain as a Spring Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception
    {
        // Step 1: Get AuthenticationManagerBuilder from HttpSecurity shared objects
        AuthenticationManagerBuilder
                authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        // Step 2: Tell Spring Security how to load users and how to verify passwords
        authenticationManagerBuilder
                .userDetailsService(usersService)
                .passwordEncoder(bCryptPasswordEncoder);

        // Step 3: Build the AuthenticationManager using the above configuration
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        // Step 4: Create custom AuthenticationFilter (handles /login requests)
        AuthenticationFilter authenticationFilter =
                new AuthenticationFilter(
                        usersService,
                        env,
                        authenticationManager);

        // Step 5: Set the login URL from application.properties (e.g. login.url.path=/users/login)
        authenticationFilter.setFilterProcessesUrl(env.getProperty("login.url.path"));

        http
                // Disable CSRF — not needed for stateless REST APIs (cross site request)
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests
                        (auth -> auth
                        // Only allow requests to /users/** from the API Gateway IP address
                        .requestMatchers(new AntPathRequestMatcher("/users/**")).access(
                                new WebExpressionAuthorizationManager("hasIpAddress('" + env.getProperty("gateway.ip") + "')"))
                        // Allow H2 console access without authentication (for development)
                        .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                        )
                // Add JWT Authorization filter — validates JWT token on every request
                .addFilter(new AuthorizationFilter(authenticationManager, env))
                // Add custom Authentication filter — handles login and issues JWT token
                .addFilter(authenticationFilter)
                // Set the AuthenticationManager to be used by the filters
                .authenticationManager(authenticationManager)
                // Use STATELESS session — no HTTP session will be created or used
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http
                .headers(headers -> headers
                        // Allow H2 console to be displayed in a frame from the same origin
                        .frameOptions(frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }
}