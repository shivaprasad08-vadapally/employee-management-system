package com.shiva.employee_management_system.controller;

import com.shiva.employee_management_system.dto.EmployeeRequest;
import com.shiva.employee_management_system.dto.EmployeeResponse;
import com.shiva.employee_management_system.mapper.EmployeeMapper;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.shiva.employee_management_system.entity.Employee;
import com.shiva.employee_management_system.entity.EmployeeStatus;
import com.shiva.employee_management_system.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/api/employees")
@Tag(
    name = "Employee Management",
    description = "APIs for creating, retrieving, updating and deleting employees"
)
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    public EmployeeController(
            EmployeeService employeeService,
            EmployeeMapper employeeMapper) {

        this.employeeService = employeeService;
        this.employeeMapper = employeeMapper;
    }

    // =========================================================
    // CREATE EMPLOYEE
    // =========================================================

    @Operation(
        summary = "Create employee",
        description = "Creates a new employee. ADMIN role required."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Employee created successfully"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid employee data"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied"
        )
    })
    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(
            @RequestBody @Valid EmployeeRequest request) {

        Employee employee = employeeMapper.toEntity(request);

        Employee savedEmployee =
                employeeService.saveEmployee(employee);

        EmployeeResponse response =
                employeeMapper.toResponse(savedEmployee);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    // =========================================================
    // GET ALL EMPLOYEES
    // =========================================================

    @Operation(
        summary = "Get all employees",
        description = "Returns all employees. ADMIN and USER roles are allowed."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Employees retrieved successfully"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied"
        )
    })
    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {

        List<Employee> employees =
                employeeService.getAllEmployees();

        List<EmployeeResponse> responses =
                employees.stream()
                        .map(employeeMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(responses);
    }

    // =========================================================
    // GET EMPLOYEE BY ID
    // =========================================================

    @Operation(
        summary = "Get employee by ID",
        description = "Returns a single employee using the employee ID."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Employee retrieved successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Employee not found"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied"
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(
            @PathVariable Long id) {

        Employee employee =
                employeeService.getEmployeeById(id);

        EmployeeResponse response =
                employeeMapper.toResponse(employee);

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // UPDATE EMPLOYEE
    // =========================================================

    @Operation(
        summary = "Update employee",
        description = "Updates an existing employee. ADMIN role required."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Employee updated successfully"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid employee data"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Employee not found"
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable Long id,
            @RequestBody @Valid EmployeeRequest request) {

        Employee employee =
                employeeMapper.toEntity(request);

        Employee updatedEmployee =
                employeeService.updateEmployee(id, employee);

        EmployeeResponse response =
                employeeMapper.toResponse(updatedEmployee);

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // DELETE EMPLOYEE
    // =========================================================

    @Operation(
        summary = "Delete employee",
        description = "Deletes an employee using the employee ID. ADMIN role required."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Employee deleted successfully"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Employee not found"
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable Long id) {

        employeeService.deleteEmployee(id);

        return ResponseEntity.noContent().build();
    }

    // =========================================================
    // SEARCH / FILTER / PAGINATION / SORTING
    // =========================================================

    @Operation(
        summary = "Search employees",
        description = "Search employees using filters, pagination and sorting."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Employees retrieved successfully"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid search, pagination or sorting parameters"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied"
        )
    })
    @GetMapping("/search")
    public ResponseEntity<Page<EmployeeResponse>> searchEmployees(

            @RequestParam(required = false)
            String name,

            @RequestParam(required = false)
            String department,

            @RequestParam(required = false)
            String designation,

            @RequestParam(required = false)
            EmployeeStatus status,

            @RequestParam(required = false)
            BigDecimal minSalary,

            @RequestParam(required = false)
            BigDecimal maxSalary,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "firstName")
            String sortBy,

            @RequestParam(defaultValue = "asc")
            String sortDirection) {

        // Validate page
        if (page < 0) {
            return ResponseEntity.badRequest().build();
        }

        // Validate size
        if (size <= 0 || size > 100) {
            return ResponseEntity.badRequest().build();
        }

        // Validate salary range
        if (minSalary != null
                && maxSalary != null
                && minSalary.compareTo(maxSalary) > 0) {

            return ResponseEntity.badRequest().build();
        }

        // Allowed sorting fields
        List<String> allowedSortFields = List.of(
            "id",
            "firstName",
            "lastName",
            "email",
            "department",
            "designation",
            "salary",
            "hireDate",
            "status",
            "createdAt",
            "updatedAt"
        );

        // Validate sort field
        if (!allowedSortFields.contains(sortBy)) {
            return ResponseEntity.badRequest().build();
        }

        // Validate sort direction
        if (!sortDirection.equalsIgnoreCase("asc")
                && !sortDirection.equalsIgnoreCase("desc")) {

            return ResponseEntity.badRequest().build();
        }

        // Create sorting
        Sort sort;

        if (sortDirection.equalsIgnoreCase("desc")) {
            sort = Sort.by(sortBy).descending();
        } else {
            sort = Sort.by(sortBy).ascending();
        }

        // Create pagination
        Pageable pageable =
                PageRequest.of(page, size, sort);

        // Search employees
        Page<Employee> employees =
                employeeService.searchEmployees(
                        name,
                        department,
                        designation,
                        status,
                        minSalary,
                        maxSalary,
                        pageable
                );

        // Convert Entity -> Response DTO
        Page<EmployeeResponse> response =
                employees.map(employeeMapper::toResponse);

        return ResponseEntity.ok(response);
    }
}