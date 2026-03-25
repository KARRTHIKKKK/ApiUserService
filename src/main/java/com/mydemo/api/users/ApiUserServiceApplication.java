package com.mydemo.api.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiUserServiceApplication
{
	public static void main(String[] args)
    {
		SpringApplication.run(ApiUserServiceApplication.class, args);
	}

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
//H2-DB  http://localhost:8080/h2-console
//Status http://localhost:8080/users/status
//Via ApiGateway http://localhost:8040/userservice/users/status