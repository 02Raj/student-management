package com.example.schoolmanagement.controller;



import com.example.schoolmanagement.dto.ApiResponse;
import com.example.schoolmanagement.dto.UserDTO;
import com.example.schoolmanagement.model.User;
import com.example.schoolmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")

public class AuthController {

    private final UserService userService;


    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup(@RequestBody UserDTO userDTO) {
        try {
            ApiResponse response = userService.registerUser(userDTO);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(false, e.getMessage(), 400, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/signin")
    public ResponseEntity<ApiResponse> signin(@RequestBody UserDTO userDTO) {
        try {
            ApiResponse response = userService.login(userDTO.getUsername(), userDTO.getPassword());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(false, e.getMessage(), 401, null);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse> getAllUsers() {
        ApiResponse response = userService.getAllUsers();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
