package com.example.schoolmanagement.dto;


public class TeacherDTO {

    private Long id;
    private String employeeNo;
    private String firstName;
    private String lastName;
    private String email;


    public TeacherDTO() {
    }

    public TeacherDTO(Long id, String employeeNo, String firstName, String lastName, String email) {
        this.id = id;
        this.employeeNo = employeeNo;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
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
}

