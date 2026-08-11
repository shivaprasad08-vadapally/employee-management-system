package com.shiva.employee_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.shiva.employee_management_system.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>,JpaSpecificationExecutor<Employee> {
    boolean existsByEmailIgnoreCase(String email);
        
    
}