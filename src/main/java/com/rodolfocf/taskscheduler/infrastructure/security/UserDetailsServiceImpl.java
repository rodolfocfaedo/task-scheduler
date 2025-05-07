package com.rodolfocf.taskscheduler.infrastructure.security;

import com.rodolfocf.taskscheduler.business.dto.ClientDTO;
import com.rodolfocf.taskscheduler.infrastructure.client.ClientClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl{

    @Autowired
    private ClientClient clientClient;

    public UserDetails loadUserByUsername(String email, String token){
        ClientDTO clientDTO = clientClient.searchClientByEmail(email, token);
        return  User
                .withUsername(clientDTO.getEmail())
                .password(clientDTO.getPassword())
                .build();
    }
}
