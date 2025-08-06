package com.example.schoolmanagement.service.impl;

import com.example.schoolmanagement.dto.StudentDTO;
import com.example.schoolmanagement.model.Student;
import com.example.schoolmanagement.model.User;
import com.example.schoolmanagement.repository.StudentRepository;
import com.example.schoolmanagement.repository.UserRepository;
import com.example.schoolmanagement.service.KafkaProducerService;
import com.example.schoolmanagement.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final KafkaProducerService kafkaProducerService;

    public StudentServiceImpl(StudentRepository studentRepository, UserRepository userRepository, KafkaProducerService kafkaProducerService) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public StudentDTO createStudent(StudentDTO studentDTO) throws Exception {
        Student student = new Student();
        student.setEnrollmentNo(studentDTO.getEnrollmentNo());
        student.setFirstName(studentDTO.getFirstName());
        student.setLastName(studentDTO.getLastName());
        student.setEmail(studentDTO.getEmail());

        if (studentDTO.getTeacherId() != null) {
            User teacher = userRepository.findById(studentDTO.getTeacherId())
                    .orElseThrow(() -> new Exception("Teacher not found with id: " + studentDTO.getTeacherId()));
            student.setTeacher(teacher);
        }

        Student savedStudent = studentRepository.save(student);

        // Produce Kafka event (as JSON)
        try {
            ObjectMapper mapper = new ObjectMapper();
            String event = mapper.writeValueAsString(mapToDTO(savedStudent));
            kafkaProducerService.sendMessage("StudentCreated:" + event);
        } catch (Exception e) {
            System.err.println("Failed to send Kafka event: " + e.getMessage());
        }

        return mapToDTO(savedStudent);
    }

    @Override
    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) throws Exception {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new Exception("Student not found with id: " + id));

        student.setEnrollmentNo(studentDTO.getEnrollmentNo());
        student.setFirstName(studentDTO.getFirstName());
        student.setLastName(studentDTO.getLastName());
        student.setEmail(studentDTO.getEmail());

        if (studentDTO.getTeacherId() != null) {
            User teacher = userRepository.findById(studentDTO.getTeacherId())
                    .orElseThrow(() -> new Exception("Teacher not found with id: " + studentDTO.getTeacherId()));
            student.setTeacher(teacher);
        } else {
            student.setTeacher(null);
        }

        Student updatedStudent = studentRepository.save(student);

        // Optionally, send Kafka event for update
        // ObjectMapper mapper = new ObjectMapper();
        // String event = mapper.writeValueAsString(mapToDTO(updatedStudent));
        // kafkaProducerService.sendMessage("StudentUpdated:" + event);

        return mapToDTO(updatedStudent);
    }

    @Override
    public void deleteStudent(Long id) throws Exception {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new Exception("Student not found with id: " + id));
        studentRepository.delete(student);

        // Optionally, send Kafka event for delete
        // kafkaProducerService.sendMessage("StudentDeleted: {\"id\":" + id + "}");
    }

    @Override
    @Cacheable(value = "student", key = "#id")
    public StudentDTO getStudentById(Long id) throws Exception {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new Exception("Student not found with id: " + id));
        return mapToDTO(student);
    }

    @Override
    @Cacheable(value = "studentsList")
    public List<StudentDTO> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Helper method to convert Student entity to StudentDTO
    private StudentDTO mapToDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setEnrollmentNo(student.getEnrollmentNo());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setEmail(student.getEmail());
        if (student.getTeacher() != null) {
            dto.setTeacherId(student.getTeacher().getId());
        }
        return dto;
    }
}
