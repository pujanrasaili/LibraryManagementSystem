# Digital Library Management System

A desktop Library Management System built with Java, JavaFX, and MySQL as part of the OOP Architecture and Implementation assignment (Week 9) for CIS096-1 at the University of Bedfordshire.

## Project Overview

This system helps librarians manage books, members, borrowing transactions, and fine collections through a modern and user-friendly JavaFX interface.

## Features

-  **Librarian Login** — Secure authentication system
-  **Book Management** — Add, Edit, Delete, Search books
-  **Member Management** — Register Student, Faculty, Public members
-  **Issue Book** — Issue books with automatic due date calculation
-  **Return Book** — Process returns with automatic fine calculation
-  **Fine Management** — Track and mark fines as paid
-  **Reports** — View overdue books, inventory, and member lists
-  **Dashboard** — Real-time stats and active borrowings

## Project Structure
```
src/
└── com/library/
    ├── controller/    → Main.java (JavaFX UI)
    ├── dao/           → BookDAO, MemberDAO, TransactionDAO
    ├── db/            → DatabaseConnection (Singleton), DatabaseException
    ├── model/         → Book, Member, Transaction, Fine
    └── util/          → AlertUtil
```

## Technologies Used

- **Language:** Java 25
- **UI Framework:** JavaFX 21
- **Database:** MySQL
- **Connectivity:** JDBC
- **IDE:** IntelliJ IDEA
- **Version Control:** Git & GitHub

## OOP Concepts Applied

| Concept | Implementation |
|---|---|
| Encapsulation | Private fields with getters/setters in all model classes |
| Inheritance | DatabaseException extends RuntimeException |
| Abstraction | DatabaseConnection hides connection complexity |
| Singleton Pattern | DatabaseConnection.getInstance() |
| DAO Pattern | Separate DAO classes for each entity |
| Polymorphism | getLoanDays() and getFinePerDay() in Member class |

## Loan & Fine Policy

| Member Type | Loan Period | Fine per Day |
|---|---|---|
| Student | 14 days | £5 |
| Faculty | 30 days | £3 |
| Public | 7 days | £10 |

## Database Setup
```sql
CREATE DATABASE library_db;
USE library_db;
-- Run the SQL scripts to create tables:
-- books, members, transactions, fines
```

## How to Run

1. Clone the repository
2. Open in IntelliJ IDEA
3. Add JavaFX 21 SDK and MySQL Connector JAR to libraries
4. Set VM options: `--module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml`
5. Create `library_db` database in MySQL
6. Run `Main.java`
7. Login with: **admin / library123**

## Author

**Pujan Rasaili**
Student ID: 2528898
University of Bedfordshire
Unit: CIS096-1 — Principles of Programming and Data Structures
