# Employee Salary Management System — ACME Org

A web application designed for HR Managers to manage salary distributions and analyze compensation data across 10,000 global employees.

## Tech Stack
* **Backend**: Java 21 / 17, Spring Boot 3.x, Spring Data JPA, MySQL
* **Frontend**: ReactJS, Vite, Tailwind CSS, Lucide Icons, Axios
* **Testing**: JUnit 5, Mockito
* **Database**: MySQL (Pre-seeded with 10,000 records via JDBC Batching)

---

## Key Features
* **HR Analytics Dashboard**: Real-time organizational metrics (Total Employees, Total Payroll, Average Salary, Global Region count).
* **High-Performance Data Grid**: Server-side paginated list handling 10,000 employees smoothly.
* **Search & Multi-Filtering**: Instant search by employee name, code, or email, alongside Country and Department filters.
* **Fast Startup Seeding**: Automated high-speed database seeder populating 10,000 records in under 3 seconds.

---

## How to Run Locally

### 1. Prerequisites
* JDK 17 or 21
* MySQL Server
* Node.js (v20+)

### 2. Backend Setup
1. Create MySQL Database:
   ```sql
   CREATE DATABASE salary_management_db;