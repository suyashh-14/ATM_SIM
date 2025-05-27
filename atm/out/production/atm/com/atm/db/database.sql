-- Create the ATM database
CREATE DATABASE IF NOT EXISTS atm_db;
USE atm_db;

-- Users table for ATM system
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    card_number VARCHAR(32) NOT NULL UNIQUE,
    pin VARCHAR(32) NOT NULL,
    balance DOUBLE DEFAULT 0,
    name VARCHAR(100) NOT NULL
);

--  Sample data
INSERT INTO users (card_number, pin, balance, name) VALUES
('1234567890', '1234', 1000.0, 'Suyash Kumar'),
('9876543210', '4321', 1500.0, 'Suyasi Kumari');