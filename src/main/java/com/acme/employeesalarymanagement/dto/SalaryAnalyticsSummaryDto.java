package com.acme.employeesalarymanagement.dto;

import java.math.BigDecimal;
import java.util.Map;

public record SalaryAnalyticsSummaryDto(
        long totalEmployees,
        BigDecimal totalPayroll,
        BigDecimal averageSalary,
        BigDecimal maxSalary,
        BigDecimal minSalary,
        Map<String, Long> countByCountry,
        Map<String, Long> countByDepartment
) {
}
