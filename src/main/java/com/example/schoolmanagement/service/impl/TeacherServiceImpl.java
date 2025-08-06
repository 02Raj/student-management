package com.example.schoolmanagement.service.impl;

import com.example.schoolmanagement.dto.TeacherDTO;
import com.example.schoolmanagement.model.Teacher;
import com.example.schoolmanagement.repository.TeacherRepository;
import com.example.schoolmanagement.service.KafkaProducerService;
import com.example.schoolmanagement.service.TeacherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final KafkaProducerService kafkaProducerService;

    public TeacherServiceImpl(TeacherRepository teacherRepository, KafkaProducerService kafkaProducerService) {
        this.teacherRepository = teacherRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public TeacherDTO createTeacher(TeacherDTO teacherDTO) throws Exception {
        Teacher teacher = new Teacher();
        teacher.setEmployeeNo(teacherDTO.getEmployeeNo());
        teacher.setFirstName(teacherDTO.getFirstName());
        teacher.setLastName(teacherDTO.getLastName());
        teacher.setEmail(teacherDTO.getEmail());

        Teacher savedTeacher = teacherRepository.save(teacher);

        try {
            ObjectMapper mapper = new ObjectMapper();
            String event = mapper.writeValueAsString(mapToDTO(savedTeacher));
            kafkaProducerService.sendMessage("TeacherCreated:" + event);
        } catch (Exception e) {
            System.err.println("Failed to send Kafka event for Teacher creation: " + e.getMessage());
        }

        return mapToDTO(savedTeacher);
    }

    @Override
    public TeacherDTO updateTeacher(Long id, TeacherDTO teacherDTO) throws Exception {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new Exception("Teacher not found with id: " + id));

        teacher.setEmployeeNo(teacherDTO.getEmployeeNo());
        teacher.setFirstName(teacherDTO.getFirstName());
        teacher.setLastName(teacherDTO.getLastName());
        teacher.setEmail(teacherDTO.getEmail());

        Teacher updatedTeacher = teacherRepository.save(teacher);

        try {
            ObjectMapper mapper = new ObjectMapper();
            String event = mapper.writeValueAsString(mapToDTO(updatedTeacher));
            kafkaProducerService.sendMessage("TeacherUpdated:" + event);
        } catch (Exception e) {
            System.err.println("Failed to send Kafka event for Teacher update: " + e.getMessage());
        }

        return mapToDTO(updatedTeacher);
    }

    @Override
    public void deleteTeacher(Long id) throws Exception {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new Exception("Teacher not found with id: " + id));
        teacherRepository.delete(teacher);

        try {
            kafkaProducerService.sendMessage("TeacherDeleted:{\"id\":" + id + "}");
        } catch (Exception e) {
            System.err.println("Failed to send Kafka event for Teacher deletion: " + e.getMessage());
        }
    }

    @Override
    public TeacherDTO getTeacherById(Long id) throws Exception {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new Exception("Teacher not found with id: " + id));
        return mapToDTO(teacher);
    }

    @Override
    @Cacheable(value = "teachersList")
    public List<TeacherDTO> getAllTeachers() {
        List<Teacher> teachers = teacherRepository.findAll();
        return teachers.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private TeacherDTO mapToDTO(Teacher teacher) {
        TeacherDTO dto = new TeacherDTO();
        dto.setId(teacher.getId());
        dto.setEmployeeNo(teacher.getEmployeeNo());
        dto.setFirstName(teacher.getFirstName());
        dto.setLastName(teacher.getLastName());
        dto.setEmail(teacher.getEmail());
        return dto;
    }
}
