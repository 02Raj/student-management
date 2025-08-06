package com.example.schoolmanagement.service;

import com.example.schoolmanagement.dto.ApiResponse;
import com.example.schoolmanagement.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {

    ApiResponse registerUser(UserDTO userDTO) throws Exception;


    ApiResponse login(String username, String password) throws Exception;

    ApiResponse getAllUsers();
}

