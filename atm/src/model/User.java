package com.atm.model;

public class User {
    private int userId;
    private String cardNumber;
    private String pin;
    private double balance;
    private String name;

    // No-argument constructor
    public User() {}

    // All-argument constructor
    public User(int userId, String cardNumber, String pin, double balance, String name) {
        this.userId = userId;
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
        this.name = name;
    }

    // Getters and setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}