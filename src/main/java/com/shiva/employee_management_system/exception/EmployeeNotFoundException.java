package com.shiva.employee_management_system.exception;

public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(Long id) {
        super("Employee with ID " + id + " not found");
    }
}