package com.shiva.employee_management_system.specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.shiva.employee_management_system.entity.Employee;
import com.shiva.employee_management_system.entity.EmployeeStatus;

public class EmployeeSpecification {

    public static Specification<Employee> hasName(String name) {

        return (root, query, criteriaBuilder) -> {

            if (name == null || name.isBlank()) {
                return null;
            }

            String searchName = "%" + name.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("firstName")),
                            searchName
                    ),
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("lastName")),
                            searchName
                    )
            );
        };
    }

    public static Specification<Employee> hasDepartment(String department) {

        return (root, query, criteriaBuilder) -> {

            if (department == null || department.isBlank()) {
                return null;
            }

            return criteriaBuilder.equal(
                    criteriaBuilder.lower(root.get("department")),
                    department.toLowerCase()
            );
        };
    }

    public static Specification<Employee> hasDesignation(String designation) {

        return (root, query, criteriaBuilder) -> {

            if (designation == null || designation.isBlank()) {
                return null;
            }

            return criteriaBuilder.equal(
                    criteriaBuilder.lower(root.get("designation")),
                    designation.toLowerCase()
            );
        };
    }

    public static Specification<Employee> hasStatus(EmployeeStatus status) {

        return (root, query, criteriaBuilder) -> {

            if (status == null) {
                return null;
            }

            return criteriaBuilder.equal(
                    root.get("status"),
                    status
            );
        };
    }

    public static Specification<Employee> salaryGreaterThanOrEqualTo(
            BigDecimal minSalary) {

        return (root, query, criteriaBuilder) -> {

            if (minSalary == null) {
                return null;
            }

            return criteriaBuilder.greaterThanOrEqualTo(
                    root.get("salary"),
                    minSalary
            );
        };
    }

    public static Specification<Employee> salaryLessThanOrEqualTo(
            BigDecimal maxSalary) {

        return (root, query, criteriaBuilder) -> {

            if (maxSalary == null) {
                return null;
            }

            return criteriaBuilder.lessThanOrEqualTo(
                    root.get("salary"),
                    maxSalary
            );
        };
    }
}