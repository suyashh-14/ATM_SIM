package com.atm.dao;

import com.atm.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private final Connection conn;

    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    public User authenticateUser(String cardNumber, String pin) throws SQLException {
        String sql = "SELECT * FROM users WHERE card_number=? AND pin=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cardNumber);
            stmt.setString(2, pin);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setCardNumber(rs.getString("card_number"));
                user.setPin(rs.getString("pin"));
                user.setBalance(rs.getDouble("balance"));
                user.setName(rs.getString("name"));
                return user;
            }
            return null;
        }
    }

    public User findUserByCardNumber(String cardNumber) throws SQLException {
        String sql = "SELECT * FROM users WHERE card_number=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cardNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setCardNumber(rs.getString("card_number"));
                user.setPin(rs.getString("pin"));
                user.setBalance(rs.getDouble("balance"));
                user.setName(rs.getString("name"));
                return user;
            }
            return null;
        }
    }

    public void updateBalance(int userId, double newBalance) throws SQLException {
        String sql = "UPDATE users SET balance=? WHERE user_id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, newBalance);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }

    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO users (card_number, pin, balance, name) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getCardNumber());
            stmt.setString(2, user.getPin());
            stmt.setDouble(3, user.getBalance());
            stmt.setString(4, user.getName());
            stmt.executeUpdate();
        }
    }

    public void updateCardNumber(int userId, String newCardNumber) throws SQLException {
        String sql = "UPDATE users SET card_number=? WHERE user_id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newCardNumber);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }

    public void updateCardholderName(int userId, String newName) throws SQLException {
        String sql = "UPDATE users SET name=? WHERE user_id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setCardNumber(rs.getString("card_number"));
                user.setPin(rs.getString("pin"));
                user.setBalance(rs.getDouble("balance"));
                user.setName(rs.getString("name"));
                users.add(user);
            }
        }
        return users;
    }
}