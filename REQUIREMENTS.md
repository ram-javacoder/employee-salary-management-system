# Requirements Document: Employee Salary Management System (ACME Org)

## 1. Goal & Product Vision
The objective of this application is to replace error-prone Excel-based salary tracking for ACME Org with a web-based management platform. 
The software enables the HR Manager to manage salary data for **10,000 employees** across multiple global regions and gain real-time visibility into organizational compensation distributions, metrics, and trends.

---

## 2. Target User Persona
* **HR Manager**: Primary administrative user who manages employee records, reviews salary structures, filters regional compensation, and generates organizational salary insights.

---

## 3. Scope & Key Features

### Functional Scope
1. **HR Analytics & Insights Dashboard**
    * **Total Payroll Summary**: Aggregated metrics for total annual payroll, average salary, median salary, highest pay, and lowest pay.
    * **Regional & Departmental Breakdown**: Instant analysis of pay across countries and departments to answer "how the org pays people".
    * **Currency Handling**: Normalized display of salaries for comparison.

2. **Employee Directory & Salary Management**
    * **High-Performance Data Grid**: Server-side paginated list handling 10,000 records smoothly without browser lag.
    * **Search & Multi-Filter**: Filter employees by country, department, job title, and salary range.
    * **CRUD Operations**: Ability to view, add, update, and delete individual employee salary details.

3. **Data Seeding & Bulk Operations**
    * **Automated Data Seeding**: Spring Boot startup seeder generating 10,000 realistic employee salary records.
    * **Bulk Import/Export**: Capability to export filtered data to CSV/Excel for external reporting.

---

## 4. Deliberate Non-Goals & Out-of-Scope (Trade-offs & Reasoning)

| Feature | Status | Reasoning |
| :--- | :--- | :--- |
| **Employee Self-Service Portal** | Out of Scope | The problem statement focuses strictly on the HR Manager persona. Adding employee login/access adds unnecessary complexity without fulfilling core requirements. |
| **Real-time Live Forex API** | Out of Scope | Multi-currency conversions use static exchange rate tables in the database. Live FX APIs introduce non-deterministic external dependencies and network latency. |
| **Complex Tax & Benefit Engine** | Out of Scope | The system targets compensation management (Base Salary, Bonus, Allowances) rather than regional payroll tax processing (PF, TDS, 401k), keeping business logic clean and deterministic. |
| **Microservices Architecture** | Out of Scope | A clean, modular Spring Boot monolith with proper indexing easily handles 10,000 records with sub-100ms response times, minimizing infrastructure overhead on limited dev setups. |

---

## 5. Technical Constraints & Stack
* **Backend**: Java 17, Spring Boot 3.x, Spring Data JPA, MySQL
* **Frontend**: ReactJS, Tailwind CSS, Shadcn UI
* **Performance Benchmark**: All database queries and search endpoints must respond under 200 ms for 10,000 records using proper SQL indexing.