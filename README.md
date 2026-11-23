# Credit Card Expense Tracker

A console-based Java application to track credit card spending.

## Features
- Register and login with username and password
- Add, view and delete transactions (CRUD)
- Category-wise spending report
- Alert when spending reaches 80% of the credit limit
- Input validation and exception handling
- Expenses saved to a file for each user (File I/O)

## Concepts Used
- OOP: classes User, Transaction, Category, AuthService, ExpenseService
- Collections: ArrayList (transactions) and HashMap (users, category totals)
- Enum, exception handling, File I/O (BufferedReader, PrintWriter)

## How to Run
1. Install Java (JDK 17 or newer)
2. Clone this repo
3. Open it in IntelliJ IDEA
4. Run `Main.java`

## Project Structure
- `Category.java`: fixed list of spending categories
- `Transaction.java`: one expense record
- `User.java`: user account and credit limit
- `AuthService.java`: register and login
- `ExpenseService.java`: expense logic, report, limit alert, save and load
- `Main.java`: menu the user interacts with

