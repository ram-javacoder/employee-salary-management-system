package com.acme.employeesalarymanagement.service;

import com.acme.employeesalarymanagement.dto.EmployeeCreateDto;
import com.acme.employeesalarymanagement.dto.EmployeeResponseDto;
import com.acme.employeesalarymanagement.dto.SalaryAnalyticsSummaryDto;
import com.acme.employeesalarymanagement.entity.Employee;
import com.acme.employeesalarymanagement.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Page<EmployeeResponseDto> getEmployees(
            String search, String country, String department,
            int page, int size, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Employee> spec = (root, query, cb) -> cb.conjunction();

        if (country != null && !country.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("country"), country));
        }

        if (department != null && !department.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("department"), department));
        }

        if (search != null && !search.isBlank()) {
            String searchTerm = "%" + search.toLowerCase().trim() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                    // Concatenate firstName + space + lastName for full name search
                    cb.like(cb.lower(cb.concat(cb.concat(root.get("firstName"), " "), root.get("lastName"))), searchTerm),
                    cb.like(cb.lower(root.get("firstName")), searchTerm),
                    cb.like(cb.lower(root.get("lastName")), searchTerm),
                    cb.like(cb.lower(root.get("employeeCode")), searchTerm),
                    cb.like(cb.lower(root.get("email")), searchTerm)
            ));
        }

        return employeeRepository.findAll(spec, pageable).map(this::mapToDto);
    }

    public SalaryAnalyticsSummaryDto getAnalyticsSummary() {
        List<Employee> allEmployees = employeeRepository.findAll();
        long totalCount = allEmployees.size();

        if (totalCount == 0) {
            return new SalaryAnalyticsSummaryDto(0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, Map.of(), Map.of());
        }

        BigDecimal totalPayroll = allEmployees.stream()
                .map(e -> e.getBaseSalary().add(e.getBonus() != null ? e.getBonus() : BigDecimal.ZERO))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal avgSalary = totalPayroll.divide(BigDecimal.valueOf(totalCount), 2, RoundingMode.HALF_UP);

        BigDecimal maxSalary = allEmployees.stream()
                .map(Employee::getBaseSalary)
                .max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);

        BigDecimal minSalary = allEmployees.stream()
                .map(Employee::getBaseSalary)
                .min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);

        Map<String, Long> countryDistribution = allEmployees.stream()
                .collect(Collectors.groupingBy(Employee::getCountry, Collectors.counting()));

        Map<String, Long> deptDistribution = allEmployees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment, Collectors.counting()));

        return new SalaryAnalyticsSummaryDto(totalCount, totalPayroll, avgSalary, maxSalary, minSalary, countryDistribution, deptDistribution);
    }

    @Transactional
    public EmployeeResponseDto createEmployee(EmployeeCreateDto dto) {
        if (employeeRepository.existsByEmployeeCode(dto.employeeCode())) {
            throw new IllegalArgumentException("Employee code already exists: " + dto.employeeCode());
        }

        Employee employee = new Employee(
                dto.employeeCode(),
                dto.firstName(),
                dto.lastName(),
                dto.email(),
                dto.department(),
                dto.jobTitle(),
                dto.country(),
                dto.currency(),
                dto.baseSalary(),
                dto.bonus() != null ? dto.bonus() : BigDecimal.ZERO,
                dto.joiningDate() != null ? dto.joiningDate() : LocalDate.now()
        );

        Employee saved = employeeRepository.save(employee);
        return mapToDto(saved);
    }

    private EmployeeResponseDto mapToDto(Employee e) {
        return new EmployeeResponseDto(
                e.getId(), e.getEmployeeCode(), e.getFirstName(), e.getLastName(),
                e.getEmail(), e.getDepartment(), e.getJobTitle(), e.getCountry(),
                e.getCurrency(), e.getBaseSalary(), e.getBonus(), e.getJoiningDate()
        );
    }
}
