package com.shiva.employee_management_system.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.shiva.employee_management_system.entity.EmployeeStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String department;

    private String designation;

    private BigDecimal salary;

    private LocalDate hireDate;

    private EmployeeStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}