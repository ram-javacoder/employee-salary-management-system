package com.acme.employeesalarymanagement.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeResponseDto(
        Long id,
        String employeeCode,
        String firstName,
        String lastName,
        String email,
        String department,
        String jobTitle,
        String country,
        String currency,
        BigDecimal baseSalary,
        BigDecimal bonus,
        LocalDate joiningDate
) {
}
