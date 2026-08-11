package com.shiva.employee_management_system.exception;

public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String email) {
        super("Employee with email '" + email + "' already exists");
    }
}