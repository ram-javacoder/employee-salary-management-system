package com.acme.employeesalarymanagement.controller;

import com.acme.employeesalarymanagement.dto.EmployeeCreateDto;
import com.acme.employeesalarymanagement.dto.EmployeeResponseDto;
import com.acme.employeesalarymanagement.dto.SalaryAnalyticsSummaryDto;
import com.acme.employeesalarymanagement.service.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employees")
@CrossOrigin(origins = "*")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<Page<EmployeeResponseDto>> getEmployees(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String department,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Page<EmployeeResponseDto> employees = employeeService.getEmployees(search, country, department, page, size, sortBy, sortDir);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/analytics/summary")
    public ResponseEntity<SalaryAnalyticsSummaryDto> getAnalyticsSummary() {
        return ResponseEntity.ok(employeeService.getAnalyticsSummary());
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDto> createEmployee(@RequestBody EmployeeCreateDto dto) {
        EmployeeResponseDto created = employeeService.createEmployee(dto);
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(created);
    }
}
