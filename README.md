# Credit Card Expense Tracker

A console-based Java application to track credit card spending, backed by a SQL database.

## Features
- Register and login with username and password (stored in a database)
- Add, view and delete transactions (CRUD)
- Category-wise spending report using SQL GROUP BY
- Alert when spending reaches 80% of the credit limit
- Input validation and exception handling
- Data persists between runs using SQLite

## Concepts Used
- OOP: User, Transaction, Category, AuthService, ExpenseService, Database
- JDBC with SQLite: Connection, PreparedStatement, ResultSet
- SQL: CREATE TABLE, INSERT, SELECT, DELETE, SUM, GROUP BY, PRIMARY KEY, FOREIGN KEY
- PreparedStatements to prevent SQL injection
- Enum, exception handling

## Database Tables
- `users` (username, password, credit_limit)
- `transactions` (id, username, description, amount, category, date)

## How to Run
1. Install Java (JDK 17 or newer)
2. Download the `sqlite-jdbc` jar and add it to the project libraries
3. Clone this repo and open it in IntelliJ IDEA
4. Run `Main.java` (the database file is created automatically)

## Project Structure
- `Database.java`: opens the connection and creates the tables
- `AuthService.java`: register and login using SQL
- `ExpenseService.java`: add, show, delete, total, category report and limit alert using SQL
- `Transaction.java`, `User.java`, `Category.java`: data classes
- `Main.java`: console menu
