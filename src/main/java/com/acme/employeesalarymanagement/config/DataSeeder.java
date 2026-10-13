package com.acme.employeesalarymanagement.config;

import com.acme.employeesalarymanagement.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
public class DataSeeder implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final JdbcTemplate jdbcTemplate;

    public DataSeeder(EmployeeRepository employeeRepository, JdbcTemplate jdbcTemplate) {
        this.employeeRepository = employeeRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {

        if (employeeRepository.count() > 0) {
            System.out.println("Data already present. Skipping data seeding.");
            return;
        }

        System.out.println("Starting high-speed data seeding for 10,000 employees...");
        long startTime = System.currentTimeMillis();

        String[] firstNames = {"Aarav", "Priya", "John", "Emma", "Siddharth", "Meera", "Liam", "Sophia", "Wei", "Akira", "Carlos", "Fatima"};
        String[] lastNames = {"Sharma", "Verma", "Smith", "Johnson", "Patel", "Nair", "Brown", "Davis", "Zhang", "Tanaka", "Gomez", "Khan"};
        String[] departments = {"Engineering", "Product", "Sales", "Human Resources", "Finance", "Marketing", "Legal"};

        // Map titles strictly to departments to avoid mismatched roles
        Map<String, List<String>> departmentTitles = Map.of(
                "Engineering", List.of("Software Engineer", "Senior Engineer", "DevOps Lead", "QA Engineer"),
                "Sales", List.of("Sales Executive", "Account Executive", "Sales Manager", "Business Development Manager"),
                "Product", List.of("Product Manager", "Product Owner", "UI/UX Designer"),
                "Marketing", List.of("Marketing Lead", "SEO Specialist", "Content Strategist"),
                "Finance", List.of("Financial Analyst", "Accountant", "Finance Manager"),
                "Human Resources", List.of("HR Specialist", "Talent Acquisition Lead", "HR Manager"),
                "Legal", List.of("Legal Counsel", "Compliance Officer")
        );
        // Country and corresponding local currency
        String[][] countryCurrency = {
                {"India", "INR"},
                {"United States", "USD"},
                {"United Kingdom", "GBP"},
                {"Germany", "EUR"},
                {"Japan", "JPY"},
                {"Singapore", "SGD"}
        };

        Random random = new Random(42); // Fixed seed for reproducible test data
        String sql = "INSERT INTO employees (employee_code, first_name, last_name, email, department, job_title, country, currency, base_salary, bonus, joining_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        List<Object[]> batchParams = new ArrayList<>();
        int totalRecords = 10000;

        for (int i = 1; i <= totalRecords; i++) {
            String empCode = String.format("EMP-%05d", i);
            String fn = firstNames[random.nextInt(firstNames.length)];
            String ln = lastNames[random.nextInt(lastNames.length)];
            String email = fn.toLowerCase() + "." + ln.toLowerCase() + i + "@acme.com";

            // Pick department first
            String dept = departments[random.nextInt(departments.length)];

            // Pick job title matching selected department
            List<String> titlesForDept = departmentTitles.get(dept);
            String title = titlesForDept.get(random.nextInt(titlesForDept.size()));

            String[] location = countryCurrency[random.nextInt(countryCurrency.length)];
            String country = location[0];
            String currency = location[1];

            // Base salary scaled reasonably per currency
            double baseAmount = switch (currency) {
                case "USD", "EUR", "GBP" -> 50000 + random.nextInt(100000);
                case "INR" -> 600000 + random.nextInt(2500000);
                case "JPY" -> 4000000 + random.nextInt(8000000);
                case "SGD" -> 60000 + random.nextInt(120000);
                default -> 50000;
            };

            BigDecimal baseSalary = BigDecimal.valueOf(baseAmount).setScale(2, RoundingMode.HALF_UP);

            // Corrected bonus calculation: baseAmount * (0.05 to 0.20)
            BigDecimal bonus = BigDecimal.valueOf(baseAmount * (0.05 + random.nextDouble() * 0.15))
                    .setScale(2, RoundingMode.HALF_UP);

            LocalDate joiningDate = LocalDate.now().minusDays(random.nextInt(3650)); // Within last 10 years

            batchParams.add(new Object[]{empCode, fn, ln, email, dept, title, country, currency, baseSalary, bonus, joiningDate});

            if (batchParams.size() == 1000 || i == totalRecords) {
                jdbcTemplate.batchUpdate(sql, batchParams);
                batchParams.clear();
            }
        }

        long endTime = System.currentTimeMillis();

        // Corrected print statement calculation
        System.out.println("Successfully seeded 10,000 employee records in " + (endTime - startTime) + " ms!");
    }
}

