🕒 Smart Attendance Tracker (JavaFX + PostgreSQL)
📌 Overview

Smart Attendance Tracker is a desktop-based attendance management system developed using JavaFX and PostgreSQL.
It is designed to manage employee login/logout, attendance records, breaks, leave requests, and system activity logs in an organized and secure way.

The project is built to simulate a real-world HR attendance system and follows professional project architecture standards.

🎯 Project Goals

Automate employee attendance tracking

Reduce manual attendance errors

Maintain accurate login/logout records

Provide a structured HR-style system

Demonstrate real-world Java application development

✨ Features
🔐 Authentication

Secure login system

Password hashing

Role-based users (Admin / HR / Employee)

🕘 Attendance Management

Login & Logout time tracking

Automatic total working hours calculation

Daily attendance records

⏸️ Break Management

Start and end breaks

Break duration calculation

Prevents multiple or invalid break actions

📅 Leave & Permission Management

Apply for leave

Apply for short permissions

Approval / rejection workflow

📊 Activity Logging

Logs important system activities

Helps in auditing and monitoring user actions

📩 Mail / Notification System

✅ New User Welcome Notification (Implemented)

Triggered automatically when a new user is created

Stored in the database for tracking

⏳ Other Notifications (Planned / In Progress)

Login alerts

Password change confirmation

Leave request & approval notifications

Manual login alerts

⚠️ Currently, only the new user welcome notification is fully implemented.
Other notification types are planned as future enhancements.

🏗️ Project Architecture

The project follows MVC + DAO + Service architecture, ensuring clean separation of responsibilities.

smartattendance
│
├── controller
├── model
├── dao
├── service
├── util
├── view        (FXML files)
└── qr
    ├── QRGenerator
    ├── QRScanner
    └── QRUtils

🛠️ Tech Stack

Language: Java

UI: JavaFX, SceneBuilder

Database: PostgreSQL

Core Concepts Used:

MVC Architecture

DAO Pattern

JDBC

OOP Principles

Exception Handling

SQL & Relational Design

⚙️ How to Run the Project
1️⃣ Prerequisites

Java JDK 11 or above

JavaFX SDK

PostgreSQL

IDE (IntelliJ IDEA / Eclipse recommended)

2️⃣ Database Setup

Create database:

CREATE DATABASE smart_attendance;


Tables used:

users

attendance

breaks

leave_requests

permissions

activity_log

mail_notifications

(All tables are linked using foreign keys.)

3️⃣ Configure Database Connection

Update database credentials in the DB utility class:

String url = "jdbc:postgresql://localhost:5432/smart_attendance";
String user = "your_username";
String password = "your_password";

4️⃣ Run the Application

Import project into IDE

Configure JavaFX VM options

Run Main.java

Login and access the dashboard

🚀 Future Enhancements

Email notifications using SMTP

Face recognition login

Admin analytics dashboard

Cloud deployment

REST API version using Spring Boot

👨‍💻 Author

Akash J
Aspiring Java Backend / Full Stack Developer

GitHub: https://github.com/akasharjun2004-prog

⭐ Why This Project Is Important

This project demonstrates:

Real-world attendance system logic

Strong Java + Database integration

Clean project architecture

Ability to build enterprise-style desktop applications
