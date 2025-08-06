package com.example.schoolmanagement.service.impl;

import com.example.schoolmanagement.dto.ApiResponse;
import com.example.schoolmanagement.dto.UserDTO;
import com.example.schoolmanagement.model.Role;
import com.example.schoolmanagement.model.RoleName;
import com.example.schoolmanagement.model.User;
import com.example.schoolmanagement.repository.RoleRepository;
import com.example.schoolmanagement.repository.UserRepository;
import com.example.schoolmanagement.service.KafkaProducerService;
import com.example.schoolmanagement.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final KafkaProducerService kafkaProducerService;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           KafkaProducerService kafkaProducerService,
                           AuthenticationConfiguration authenticationConfiguration) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.kafkaProducerService = kafkaProducerService;
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Override
    public ApiResponse registerUser(UserDTO userDTO) throws Exception {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new Exception("Username is already taken");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        Set<Role> roles = new HashSet<>();
        for (String roleName : userDTO.getRoleNames()) {
            RoleName rn = RoleName.valueOf(roleName.toUpperCase());
            Role role = roleRepository.findByName(rn)
                    .orElseThrow(() -> new Exception("Role not found: " + roleName));
            roles.add(role);
        }
        user.setRoles(roles);

        User savedUser = userRepository.save(user);

        Set<String> roleNames = new HashSet<>();
        for (Role role : savedUser.getRoles()) {
            roleNames.add(role.getName().name());
        }


        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId", savedUser.getId());
        userInfo.put("username", savedUser.getUsername());
        userInfo.put("roles", roleNames);


        try {
            ObjectMapper mapper = new ObjectMapper();
            String userJson = mapper.writeValueAsString(userInfo);
            kafkaProducerService.sendMessage("UserRegistered:" + userJson);
        } catch (Exception e) {

            System.err.println("Kafka event could not be sent: " + e.getMessage());
        }

        return new ApiResponse(true, "User registered successfully", 201, userInfo);
    }

    @Override
    @Cacheable(value = "users", key = "#username")
    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    @Override
    public ApiResponse login(String username, String password) throws Exception {
        try {
            Authentication authentication = authenticationConfiguration
                    .getAuthenticationManager()
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));

            Map<String, Object> userInfo = new HashMap<>();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new Exception("User not found"));

            userInfo.put("userId", user.getId());
            userInfo.put("username", user.getUsername());
            userInfo.put("roles", user.getRoles().stream().map(r -> r.getName().name()).toList());



            return new ApiResponse(true, "User logged in successfully", 200, userInfo);

        } catch (Exception e) {
            return new ApiResponse(false, "Invalid username or password", 401, null);
        }
    }

    @Override
    @Cacheable(value = "usersList")
    public ApiResponse getAllUsers() {
        var users = userRepository.findAll();

        var userList = users.stream().map(user -> {
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId", user.getId());
            userInfo.put("username", user.getUsername());
            userInfo.put("roles", user.getRoles().stream().map(r -> r.getName().name()).toList());
            return userInfo;
        }).toList();

        return new ApiResponse(true, "Users fetched successfully", 200, userList);
    }
}
