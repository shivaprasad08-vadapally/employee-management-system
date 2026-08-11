package com.shiva.employee_management_system.mapper;

import org.springframework.stereotype.Component;

import com.shiva.employee_management_system.dto.EmployeeResponse;
import com.shiva.employee_management_system.dto.EmployeeRequest;
import com.shiva.employee_management_system.entity.Employee;

@Component
public class EmployeeMapper {

    public Employee toEntity(EmployeeRequest request) {

        return Employee.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .department(request.getDepartment())
                .designation(request.getDesignation())
                .salary(request.getSalary())
                .hireDate(request.getHireDate())
                .status(request.getStatus())
                .build();
    }
    public EmployeeResponse toResponse(Employee employee) {

    return EmployeeResponse.builder()
            .id(employee.getId())
            .firstName(employee.getFirstName())
            .lastName(employee.getLastName())
            .email(employee.getEmail())
            .phoneNumber(employee.getPhoneNumber())
            .department(employee.getDepartment())
            .designation(employee.getDesignation())
            .salary(employee.getSalary())
            .hireDate(employee.getHireDate())
            .status(employee.getStatus())
            .createdAt(employee.getCreatedAt())
            .updatedAt(employee.getUpdatedAt())
            .build();
}
}