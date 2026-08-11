package com.shiva.employee_management_system.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.shiva.employee_management_system.entity.Employee;
import com.shiva.employee_management_system.entity.EmployeeStatus;
import com.shiva.employee_management_system.exception.DuplicateEmailException;
import com.shiva.employee_management_system.exception.EmployeeNotFoundException;
import com.shiva.employee_management_system.repository.EmployeeRepository;
import com.shiva.employee_management_system.service.EmployeeService;
import com.shiva.employee_management_system.specification.EmployeeSpecification;

@Service
public class EmployeeServiceimpl implements EmployeeService {

    private static final Logger log =
            LoggerFactory.getLogger(EmployeeServiceimpl.class);

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceimpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // =========================================================
    // CREATE EMPLOYEE
    // =========================================================

    @Override
    public Employee saveEmployee(Employee employee) {

        log.info("Creating employee with email: {}",
                employee.getEmail());

        if (employeeRepository.existsByEmailIgnoreCase(
                employee.getEmail())) {

            log.warn("Duplicate email detected: {}",
                    employee.getEmail());

            throw new DuplicateEmailException(
                    employee.getEmail());
        }

        Employee savedEmployee =
                employeeRepository.save(employee);

        log.info("Employee created successfully. ID: {}",
                savedEmployee.getId());

        return savedEmployee;
    }

    // =========================================================
    // GET EMPLOYEE BY ID
    // =========================================================

    @Override
    public Employee getEmployeeById(Long id) {

        log.info("Fetching employee with ID: {}", id);

        return employeeRepository.findById(id)
                .orElseThrow(() -> {

                    log.warn(
                            "Employee not found with ID: {}",
                            id
                    );

                    return new EmployeeNotFoundException(id);
                });
    }

    // =========================================================
    // GET ALL EMPLOYEES
    // =========================================================

    @Override
    public List<Employee> getAllEmployees() {

        log.info("Fetching all employees");

        List<Employee> employees =
                employeeRepository.findAll();

        log.info("Total employees found: {}",
                employees.size());

        return employees;
    }

    // =========================================================
    // UPDATE EMPLOYEE
    // =========================================================

    @Override
    public Employee updateEmployee(
            Long id,
            Employee employee) {

        log.info("Updating employee with ID: {}", id);

        Employee existingEmployee =
                employeeRepository.findById(id)
                        .orElseThrow(() -> {

                            log.warn(
                                    "Employee not found for update. ID: {}",
                                    id
                            );

                            return new EmployeeNotFoundException(id);
                        });

        /*
         * Check whether the new email already belongs
         * to another employee.
         *
         * If the email belongs to the same employee,
         * updating is allowed.
         */

        if (!existingEmployee.getEmail()
                .equalsIgnoreCase(employee.getEmail())) {

            log.info(
                    "Checking whether new email is already in use: {}",
                    employee.getEmail()
            );

            if (employeeRepository.existsByEmailIgnoreCase(
                    employee.getEmail())) {

                log.warn(
                        "Duplicate email detected during update: {}",
                        employee.getEmail()
                );

                throw new DuplicateEmailException(
                        employee.getEmail());
            }
        }

        existingEmployee.setFirstName(
                employee.getFirstName());

        existingEmployee.setLastName(
                employee.getLastName());

        existingEmployee.setEmail(
                employee.getEmail());

        existingEmployee.setPhoneNumber(
                employee.getPhoneNumber());

        existingEmployee.setDepartment(
                employee.getDepartment());

        existingEmployee.setDesignation(
                employee.getDesignation());

        existingEmployee.setSalary(
                employee.getSalary());

        existingEmployee.setHireDate(
                employee.getHireDate());

        existingEmployee.setStatus(
                employee.getStatus());

        Employee updatedEmployee =
                employeeRepository.save(existingEmployee);

        log.info(
                "Employee updated successfully. ID: {}",
                updatedEmployee.getId()
        );

        return updatedEmployee;
    }

    // =========================================================
    // DELETE EMPLOYEE
    // =========================================================

    @Override
    public void deleteEmployee(Long id) {

        log.info("Deleting employee with ID: {}", id);

        Employee employee =
                employeeRepository.findById(id)
                        .orElseThrow(() -> {

                            log.warn(
                                    "Employee not found for deletion. ID: {}",
                                    id
                            );

                            return new EmployeeNotFoundException(id);
                        });

        employeeRepository.delete(employee);

        log.info(
                "Employee deleted successfully. ID: {}",
                id
        );
    }

    // =========================================================
    // SEARCH / FILTER / PAGINATION / SORTING
    // =========================================================

    @Override
    public Page<Employee> searchEmployees(
            String name,
            String department,
            String designation,
            EmployeeStatus status,
            BigDecimal minSalary,
            BigDecimal maxSalary,
            Pageable pageable) {

        log.info(
                "Searching employees. name={}, department={}, designation={}, status={}, minSalary={}, maxSalary={}, page={}, size={}",
                name,
                department,
                designation,
                status,
                minSalary,
                maxSalary,
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Specification<Employee> specification =
                Specification
                        .where(EmployeeSpecification.hasName(name))
                        .and(EmployeeSpecification.hasDepartment(department))
                        .and(EmployeeSpecification.hasDesignation(designation))
                        .and(EmployeeSpecification.hasStatus(status))
                        .and(EmployeeSpecification
                                .salaryGreaterThanOrEqualTo(minSalary))
                        .and(EmployeeSpecification
                                .salaryLessThanOrEqualTo(maxSalary));

        Page<Employee> result =
                employeeRepository.findAll(
                        specification,
                        pageable
                );

        log.info(
                "Employee search completed. {} employees found on current page",
                result.getNumberOfElements()
        );

        return result;
    }
}