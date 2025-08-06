package com.example.schoolmanagement.service;



import com.example.schoolmanagement.dto.TeacherDTO;

import java.util.List;

public interface TeacherService {

    TeacherDTO createTeacher(TeacherDTO teacherDTO) throws Exception;

    TeacherDTO updateTeacher(Long id, TeacherDTO teacherDTO) throws Exception;

    void deleteTeacher(Long id) throws Exception;

    TeacherDTO getTeacherById(Long id) throws Exception;

    List<TeacherDTO> getAllTeachers();
}

