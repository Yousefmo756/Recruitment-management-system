# Recruitment Management System

A Java-based recruitment management application built using core Object-Oriented Programming principles. Developed as part of an OOP course at Misr International University.

## What It Does

The system simulates a real-world recruitment workflow with three types of users — Admins, Companies, and Applicants — each with their own set of permissions and actions.

**Applicants** can:
- Apply for job recruitments (with duplicate prevention)
- View the status of their submitted applications

**Companies** can:
- Post, update, and delete job recruitments
- View applicants for each recruitment
- Accept or reject applicants
- Schedule interviews with date and time
- Find the most applied-for job within a date range

**Admins** can:
- Add, edit, delete, and search users
- View recruitment details and application counts
- Find the top applied company across the platform
- Get the average number of recruitments per month for a company
- Find the applicant with the most submissions

## Key Concepts Used

- **Inheritance** — `Applicant`, `Company`, and `Admin` all extend a base `User` class
- **Polymorphism** — shared methods overridden per user type
- **Encapsulation** — private fields accessed via getters/setters across all classes
- **Serialization** — data is saved and loaded from disk using `ObjectOutputStream` / `ObjectInputStream` via a dedicated `FileHandler` class
- **Exception Handling** — custom exceptions for invalid operations (duplicate applications, missing users, etc.)
- **Java Collections** — `ArrayList` and iteration used throughout for managing users, recruitments, and applications

## Tech Stack

- Java
- NetBeans IDE
- Java I/O & Serialization (`java.io.Serializable`)
- `java.time.LocalDate` / `LocalDateTime` for date handling

## Project Structure

```
├── User.java          # Base class for all user types
├── Applicant.java     # Applicant user — apply for jobs, view status
├── Company.java       # Company user — manage recruitments and applicants
├── Applicant.java     # Admin user — platform-wide management
├── recruitment.java   # Recruitment/job posting entity
├── Application1.java  # Application entity linking applicant to recruitment
├── FileHandler.java   # Handles saving and loading all data to disk
└── JavaApplication9.java  # Main entry point
```
