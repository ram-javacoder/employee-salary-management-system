package com.acme.employeesalarymanagement.service;

import com.acme.employeesalarymanagement.dto.SalaryAnalyticsSummaryDto;
import com.acme.employeesalarymanagement.entity.Employee;
import com.acme.employeesalarymanagement.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee emp1;
    private Employee emp2;

    @BeforeEach
    void setUp() {
        emp1 = new Employee("EMP-001", "Aarav", "Sharma", "aarav@acme.com",
                "Engineering", "Software Engineer", "India", "INR",
                new BigDecimal("1000000.00"), new BigDecimal("100000.00"), LocalDate.now());

        emp2 = new Employee("EMP-002", "John", "Smith", "john@acme.com",
                "Sales", "Product Manager", "United States", "USD",
                new BigDecimal("80000.00"), new BigDecimal("10000.00"), LocalDate.now());
    }

    @Test
    @DisplayName("Should correctly calculate organizational analytics summary")
    void testGetAnalyticsSummary_Success() {
        when(employeeRepository.findAll()).thenReturn(List.of(emp1, emp2));

        SalaryAnalyticsSummaryDto analytics = employeeService.getAnalyticsSummary();

        assertNotNull(analytics);
        assertEquals(2, analytics.totalEmployees());
        assertEquals(new BigDecimal("1190000.00"), analytics.totalPayroll()); // 1,100,000 + 90,000
        assertEquals(1, analytics.countByCountry().get("India"));
        assertEquals(1, analytics.countByCountry().get("United States"));

        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty metrics when no employees exist")
    void testGetAnalyticsSummary_EmptyList() {
        when(employeeRepository.findAll()).thenReturn(List.of());

        SalaryAnalyticsSummaryDto analytics = employeeService.getAnalyticsSummary();

        assertNotNull(analytics);
        assertEquals(0, analytics.totalEmployees());
        assertEquals(BigDecimal.ZERO, analytics.totalPayroll());

        verify(employeeRepository, times(1)).findAll();
    }
}
