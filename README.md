# ATM Simulator (Java + MySQL)

A simple desktop ATM simulator with a Java Swing interface and MySQL database.

## Features
- Admin control
- Login with card number and PIN
- Check balance
- Deposit and withdraw money
- Simulates a real ATM UI

## Structure

```
src/
  main/
    java/
      com/atm/db/DBConnection.java
      com/atm/model/User.java
      com/atm/service/ATMService.java
      com/atm/ui/ATMApp.java
database/
  atm.sql
README.md
```

## Setup

1. **Database:**
   - Run `database/atm.sql` in your MySQL to set up tables and demo users.

2. **Java:**
   - Use Java 8+.
   - Add the MySQL JDBC Driver to your project’s classpath.

3. **Build & Run:**
   - Compile all Java files in `src/main/java`.
   - Run `com.atm.ui.ATMApp` as your main class.

4. **Login:**
   - Use demo users from `atm.sql` (e.g., card: `1234567890`, PIN: `1234`).

## Customization

- Change database credentials in `DBConnection.java`.
- Add more users to `users` table as needed.

---

