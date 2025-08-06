package com.example.schoolmanagement.dto;



public class StudentDTO {

    private Long id;
    private String enrollmentNo;
    private String firstName;
    private String lastName;
    private String email;
    private Long teacherId;

    // No-arg constructor
    public StudentDTO() {
    }

    // All-args constructor
    public StudentDTO(Long id, String enrollmentNo, String firstName, String lastName, String email, Long teacherId) {
        this.id = id;
        this.enrollmentNo = enrollmentNo;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.teacherId = teacherId;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEnrollmentNo() {
        return enrollmentNo;
    }

    public void setEnrollmentNo(String enrollmentNo) {
        this.enrollmentNo = enrollmentNo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }
}

