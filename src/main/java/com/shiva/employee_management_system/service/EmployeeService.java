package com.shiva.employee_management_system.service;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.shiva.employee_management_system.entity.Employee;
import com.shiva.employee_management_system.entity.EmployeeStatus;

public interface EmployeeService {

    Employee saveEmployee(Employee employee);

    Employee getEmployeeById(Long id);

    List<Employee> getAllEmployees();

    Employee updateEmployee(Long id, Employee employee);

    void deleteEmployee(Long id);

    //List<Employee> getEmployeesByDepartment(String department);

    Page<Employee> searchEmployees(
        String name,
        String department,
        String designation,
        EmployeeStatus status,
        BigDecimal minSalary,
        BigDecimal maxSalary,
        Pageable pageable
);
    
}