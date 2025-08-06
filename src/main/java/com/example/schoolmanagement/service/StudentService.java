package com.example.schoolmanagement.service;

import com.example.schoolmanagement.dto.StudentDTO;
import java.util.List;

public interface StudentService {
    StudentDTO createStudent(StudentDTO studentDTO) throws Exception;
    StudentDTO updateStudent(Long id, StudentDTO studentDTO) throws Exception;
    void deleteStudent(Long id) throws Exception;
    StudentDTO getStudentById(Long id) throws Exception;
    List<StudentDTO> getAllStudents();
}
