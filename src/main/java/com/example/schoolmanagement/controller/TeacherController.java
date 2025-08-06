package com.example.schoolmanagement.controller;

import com.example.schoolmanagement.dto.ApiResponse;
import com.example.schoolmanagement.dto.TeacherDTO;
import com.example.schoolmanagement.service.TeacherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;


    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }


    @PostMapping
    public ResponseEntity<ApiResponse> createTeacher(@RequestBody TeacherDTO teacherDTO) {
        try {
            TeacherDTO createdTeacher = teacherService.createTeacher(teacherDTO);
            return new ResponseEntity<>(
                    new ApiResponse(true, "Teacher created successfully", 201, createdTeacher),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiResponse(false, e.getMessage(), 400, null),
                    HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getTeacherById(@PathVariable Long id) {
        try {
            TeacherDTO teacherDTO = teacherService.getTeacherById(id);
            return new ResponseEntity<>(teacherDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiResponse(false, e.getMessage(), 404, null),
                    HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping
    public ResponseEntity<List<TeacherDTO>> getAllTeachers() {
        List<TeacherDTO> teachers = teacherService.getAllTeachers();
        return new ResponseEntity<>(teachers, HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateTeacher(@PathVariable Long id, @RequestBody TeacherDTO teacherDTO) {
        try {
            TeacherDTO updatedTeacher = teacherService.updateTeacher(id, teacherDTO);
            return new ResponseEntity<>(
                    new ApiResponse(true, "Teacher updated successfully", 200, updatedTeacher),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiResponse(false, e.getMessage(), 400, null),
                    HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteTeacher(@PathVariable Long id) {
        try {
            teacherService.deleteTeacher(id);
            return new ResponseEntity<>(
                    new ApiResponse(true, "Teacher deleted successfully", 200, null),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiResponse(false, e.getMessage(), 400, null),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
