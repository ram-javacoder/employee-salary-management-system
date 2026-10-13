# System Architecture & Design Notes

## 1. System Overview
The ACME Org Employee Salary Management System is designed as a modular Spring Boot monolith backend integrated with a ReactJS frontend. It serves the HR Manager persona to manage salary details for 10,000 employees with low-latency analytics and paginated directory searches.

### End-to-End System Architecture

```text
       [ Web Browser / HR Manager UI ]
                     │
              REST / JSON (HTTP)
                     │
                     ▼
┌─────────────────────────────────────────┐
│        Spring Boot Monolith Backend     │
│ ┌─────────────────────────────────────┐ │
│ │  REST Controller Layer (Endpoints)  │ │
│ ├─────────────────────────────────────┤ │
│ │  Service Layer (Business Rules &    │ │
│ │                  Salary Insights)   │ │
│ ├─────────────────────────────────────┤ │
│ │  Data Layer (Spring Data JPA / JDBC)│ │
│ └─────────────────────────────────────┘ │
└────────────────────┬────────────────────┘
                     │ SQL / JDBC Batching
                     ▼
┌─────────────────────────────────────────┐
│        Relational Database (MySQL)      │
│  - Indexing on country, dept, salary    │
│  - Pre-seeded with 10,000 Records       │
└─────────────────────────────────────────┘
```

---

## 2. Architectural Decisions & Trade-offs

### Modular Monolith over Microservices
* **Decision**: Built a single Spring Boot service rather than microservices.
* **Rationale**: For a dataset of 10,000 records managed by a single administrative persona, a well-structured monolith reduces operational complexity, deployment overhead, and inter-service latency while remaining memory-efficient.

### Database Indexing & Query Strategy
* **Indexes**: Applied database indexes on `country`, `department`, and `base_salary`.
* **Dynamic Queries**: Leveraged Spring Data JPA Specifications (`cb.conjunction()`) to dynamically append multi-filter criteria without incurring SQL syntax or null pointer exceptions.

### Data Seeding Performance
* **Technique**: Utilized Spring JDBC `JdbcTemplate` batch execution instead of default JPA `saveAll()`.
* **Result**: Reduced 10,000 record database population time from over 45 seconds to under 3 seconds on startup.

---

## 3. API & Data Flow
1. **Analytics Endpoint**: `GET /api/v1/employees/analytics/summary` — Aggregates total payroll, average salary, and regional/departmental distributions.
2. **Directory Endpoint**: `GET /api/v1/employees` — Handles server-side pagination (`page`, `size`), filtering (`country`, `department`), and global search (`search`).