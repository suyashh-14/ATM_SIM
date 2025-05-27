package com.atm.ui;

import com.atm.dao.UserDAO;
import com.atm.db.DatabaseConnection;
import com.atm.model.User;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ATMSimulatorUI extends JFrame {
    private UserDAO userDAO;
    private User currentUser;
    private int invalidAttempts = 0;
    private static final int MAX_ATTEMPTS = 3;

    // UI fields
    private JTextField cardNumberField;
    private JPasswordField pinField;
    private JTextField amountField;
    private JTextField transferCardNumberField;
    private JTextArea receiptArea;
    private JPanel mainPanel;

    private boolean isAdmin = false;

    public ATMSimulatorUI() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            userDAO = new UserDAO(conn);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database connection failed!\n" + e.getMessage());
            System.exit(1);
        }

        setTitle("Smart ATM Simulator");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        showWelcomePanel();
    }

    // Welcome Panel - choose Admin/User
    private void showWelcomePanel() {
        mainPanel = new JPanel(new CardLayout());
        JPanel welcomePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        JButton adminButton = new JButton("Admin Login");
        JButton userButton = new JButton("Start ATM Simulation");

        adminButton.setFont(new Font("Arial", Font.BOLD, 16));
        userButton.setFont(new Font("Arial", Font.BOLD, 16));

        adminButton.addActionListener(e -> showAdminLoginPanel());
        userButton.addActionListener(e -> showUserLoginPanel());

        gbc.gridx = 0; gbc.gridy = 0;
        welcomePanel.add(adminButton, gbc);
        gbc.gridy = 1;
        welcomePanel.add(userButton, gbc);

        mainPanel.add(welcomePanel, "welcome");
        add(mainPanel);
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "welcome");
    }

    // Admin Login Panel
    private void showAdminLoginPanel() {
        JPanel adminLoginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel adminLabel = new JLabel("Admin Login", SwingConstants.CENTER);
        adminLabel.setFont(new Font("Arial", Font.BOLD, 22));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        adminLoginPanel.add(adminLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        adminLoginPanel.add(new JLabel("Username:"), gbc);
        JTextField adminUserField = new JTextField(20);
        gbc.gridx = 1;
        adminLoginPanel.add(adminUserField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        adminLoginPanel.add(new JLabel("Password:"), gbc);
        JPasswordField adminPassField = new JPasswordField(20);
        gbc.gridx = 1;
        adminLoginPanel.add(adminPassField, gbc);

        JButton loginBtn = new JButton("Login");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        adminLoginPanel.add(loginBtn, gbc);

        loginBtn.addActionListener(e -> {
            String username = adminUserField.getText().trim();
            String password = new String(adminPassField.getPassword()).trim();
            // Simple admin check; you can replace with DB or config check
            if (username.equals("admin") && password.equals("admin")) {
                isAdmin = true;
                showAdminDashboard();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid admin credentials!");
            }
        });

        JButton backBtn = new JButton("Back");
        gbc.gridy = 4;
        adminLoginPanel.add(backBtn, gbc);
        backBtn.addActionListener(e -> {
            CardLayout cl = (CardLayout) mainPanel.getLayout();
            cl.show(mainPanel, "welcome");
        });

        mainPanel.add(adminLoginPanel, "adminLogin");
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "adminLogin");
    }

    // Admin Dashboard Panel
    private void showAdminDashboard() {
        JPanel adminPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        adminPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel adminTitle = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        adminTitle.setFont(new Font("Arial", Font.BOLD, 22));
        adminPanel.add(adminTitle);

        JButton addAccountBtn = new JButton("Add New Account");
        JButton updateCardNumberBtn = new JButton("Update Card Number");
        JButton updateCardholderBtn = new JButton("Update Cardholder Name");
        JButton viewUsersBtn = new JButton("View All Users");
        JButton logoutBtn = new JButton("Logout");

        addAccountBtn.addActionListener(e -> showAddAccountDialog());
        updateCardNumberBtn.addActionListener(e -> showUpdateCardNumberDialog());
        updateCardholderBtn.addActionListener(e -> showUpdateCardholderNameDialog());
        viewUsersBtn.addActionListener(e -> showAllUsersDialog());
        logoutBtn.addActionListener(e -> {
            isAdmin = false;
            CardLayout cl = (CardLayout) mainPanel.getLayout();
            cl.show(mainPanel, "welcome");
        });

        adminPanel.add(addAccountBtn);
        adminPanel.add(updateCardNumberBtn);
        adminPanel.add(updateCardholderBtn);
        adminPanel.add(viewUsersBtn);
        adminPanel.add(logoutBtn);

        mainPanel.add(adminPanel, "adminDashboard");
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "adminDashboard");
    }

    // Dialog to add account
    private void showAddAccountDialog() {
        JTextField cardNumberInput = new JTextField(20);
        JTextField nameInput = new JTextField(20);
        JPasswordField pinInput = new JPasswordField(20);
        JTextField balanceInput = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Card Number:"));
        panel.add(cardNumberInput);
        panel.add(new JLabel("Name:"));
        panel.add(nameInput);
        panel.add(new JLabel("PIN:"));
        panel.add(pinInput);
        panel.add(new JLabel("Initial Balance:"));
        panel.add(balanceInput);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Account", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String cardNumber = cardNumberInput.getText().trim();
                String name = nameInput.getText().trim();
                String pin = new String(pinInput.getPassword()).trim();
                double balance = Double.parseDouble(balanceInput.getText().trim());

                if (cardNumber.isEmpty() || !cardNumber.matches("\\d+")) {
                    JOptionPane.showMessageDialog(this, "Invalid card number!");
                    return;
                }
                if (pin.isEmpty() || !pin.matches("\\d{4,6}")) {
                    JOptionPane.showMessageDialog(this, "Invalid PIN!");
                    return;
                }
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Name cannot be empty!");
                    return;
                }
                if (balance < 0) {
                    JOptionPane.showMessageDialog(this, "Balance cannot be negative!");
                    return;
                }
                if (userDAO.findUserByCardNumber(cardNumber) != null) {
                    JOptionPane.showMessageDialog(this, "Card number already exists!");
                    return;
                }

                User newUser = new User();
                newUser.setCardNumber(cardNumber);
                newUser.setName(name);
                newUser.setPin(pin);
                newUser.setBalance(balance);

                userDAO.addUser(newUser);
                JOptionPane.showMessageDialog(this, "Account added successfully!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid balance!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Database error!\n" + e.getMessage());
            }
        }
    }

    // Dialog to update card number
    private void showUpdateCardNumberDialog() {
        JTextField oldCardField = new JTextField(20);
        JTextField newCardField = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Existing Card Number:"));
        panel.add(oldCardField);
        panel.add(new JLabel("New Card Number:"));
        panel.add(newCardField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Update Card Number", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String oldCard = oldCardField.getText().trim();
            String newCard = newCardField.getText().trim();
            if (oldCard.isEmpty() || newCard.isEmpty() || !oldCard.matches("\\d+") || !newCard.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Invalid card numbers!");
                return;
            }
            try {
                User user = userDAO.findUserByCardNumber(oldCard);
                if (user == null) {
                    JOptionPane.showMessageDialog(this, "User not found!");
                    return;
                }
                if (userDAO.findUserByCardNumber(newCard) != null) {
                    JOptionPane.showMessageDialog(this, "New card number already in use!");
                    return;
                }
                userDAO.updateCardNumber(user.getUserId(), newCard);
                JOptionPane.showMessageDialog(this, "Card number updated!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Database error!\n" + e.getMessage());
            }
        }
    }

    // Dialog to update cardholder name
    private void showUpdateCardholderNameDialog() {
        JTextField cardField = new JTextField(20);
        JTextField nameField = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Card Number:"));
        panel.add(cardField);
        panel.add(new JLabel("New Cardholder Name:"));
        panel.add(nameField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Update Cardholder Name", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String card = cardField.getText().trim();
            String newName = nameField.getText().trim();
            if (card.isEmpty() || newName.isEmpty() || !card.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Invalid input!");
                return;
            }
            try {
                User user = userDAO.findUserByCardNumber(card);
                if (user == null) {
                    JOptionPane.showMessageDialog(this, "User not found!");
                    return;
                }
                userDAO.updateCardholderName(user.getUserId(), newName);
                JOptionPane.showMessageDialog(this, "Cardholder name updated!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Database error!\n" + e.getMessage());
            }
        }
    }

    // Dialog to view all users
    private void showAllUsersDialog() {
        try {
            java.util.List<User> users = userDAO.getAllUsers();
            StringBuilder sb = new StringBuilder();
            for (User user : users) {
                sb.append("UserID: ").append(user.getUserId()).append(", Card: ").append(user.getCardNumber())
                  .append(", Name: ").append(user.getName()).append(", Balance: ₹").append(user.getBalance()).append("\n");
            }
            JTextArea area = new JTextArea(sb.toString());
            area.setEditable(false);
            JScrollPane scroll = new JScrollPane(area);
            scroll.setPreferredSize(new Dimension(400, 200));
            JOptionPane.showMessageDialog(this, scroll, "All Users", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error!\n" + e.getMessage());
        }
    }

    // User Login Panel
    private void showUserLoginPanel() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("ATM User Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(25, 25, 112));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        loginPanel.add(new JLabel("Card Number:"), gbc);
        cardNumberField = new JTextField(20);
        gbc.gridx = 1;
        loginPanel.add(cardNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(new JLabel("PIN:"), gbc);
        pinField = new JPasswordField(20);
        gbc.gridx = 1;
        loginPanel.add(pinField, gbc);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(30, 144, 255));
        loginButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        loginPanel.add(loginButton, gbc);

        loginButton.addActionListener(e -> authenticateUser());

        JButton backBtn = new JButton("Back");
        gbc.gridy = 4;
        loginPanel.add(backBtn, gbc);
        backBtn.addActionListener(e -> {
            CardLayout cl = (CardLayout) mainPanel.getLayout();
            cl.show(mainPanel, "welcome");
        });

        mainPanel.add(loginPanel, "userLogin");
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "userLogin");
    }

    // User Authentication
    private void authenticateUser() {
        String cardNumber = cardNumberField.getText().trim();
        String pin = new String(pinField.getPassword()).trim();

        if (cardNumber.isEmpty() || !cardNumber.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Invalid card number format!");
            return;
        }
        if (pin.isEmpty() || !pin.matches("\\d{4,6}")) {
            JOptionPane.showMessageDialog(this, "Invalid PIN format!");
            return;
        }

        try {
            currentUser = userDAO.authenticateUser(cardNumber, pin);
            if (currentUser != null) {
                invalidAttempts = 0;
                cardNumberField.setText("");
                pinField.setText("");
                showUserMenuPanel();
            } else {
                invalidAttempts++;
                if (invalidAttempts >= MAX_ATTEMPTS) {
                    JOptionPane.showMessageDialog(this, "Too many invalid attempts. Account locked!");
                    System.exit(1);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid card number or PIN. Attempts left: " + (MAX_ATTEMPTS - invalidAttempts));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error!\n" + e.getMessage());
        }
    }

    // User Menu Panel
    private void showUserMenuPanel() {
        JPanel menuPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        menuPanel.setBackground(new Color(240, 248, 255));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] buttonLabels = {
            "Check Balance", "Withdraw Cash", "Deposit Funds",
            "Transfer Funds", "Passbook Printing", "Print Receipt",
            "Cancel Transaction"
        };
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setBackground(new Color(30, 144, 255));
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Arial", Font.PLAIN, 16));
            menuPanel.add(button);
            button.addActionListener(e -> handleMenuAction(label));
        }

        mainPanel.add(menuPanel, "userMenu");
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "userMenu");
    }

    private void handleMenuAction(String action) {
        switch (action) {
            case "Check Balance":
                showBalance();
                break;
            case "Withdraw Cash":
                showAmountInput("Withdraw");
                break;
            case "Deposit Funds":
                showAmountInput("Deposit");
                break;
            case "Transfer Funds":
                showTransferInput();
                break;
            case "Passbook Printing":
                printPassbook();
                break;
            case "Print Receipt":
                printReceipt();
                break;
            case "Cancel Transaction":
                cancelTransaction();
                break;
        }
    }

    private void showBalance() {
        JOptionPane.showMessageDialog(this, String.format("Current Balance: ₹%.2f", currentUser.getBalance()));
    }

    private void showAmountInput(String type) {
        amountField = new JTextField(10);
        int result = JOptionPane.showConfirmDialog(this, amountField, "Enter " + type + " Amount", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                double amount = Double.parseDouble(amountField.getText().trim());
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be positive!");
                    return;
                }
                if (type.equals("Withdraw")) {
                    if (amount > currentUser.getBalance()) {
                        JOptionPane.showMessageDialog(this, "Insufficient funds!");
                        return;
                    }
                    currentUser.setBalance(currentUser.getBalance() - amount);
                } else {
                    currentUser.setBalance(currentUser.getBalance() + amount);
                }
                userDAO.updateBalance(currentUser.getUserId(), currentUser.getBalance());
                JOptionPane.showMessageDialog(this, type + " successful!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid amount!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Database error!\n" + e.getMessage());
            }
        }
    }

    private void showTransferInput() {
        transferCardNumberField = new JTextField(20);
        amountField = new JTextField(10);
        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Recipient Card Number:"));
        panel.add(transferCardNumberField);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Transfer Funds", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String recipientCard = transferCardNumberField.getText().trim();
                double amount = Double.parseDouble(amountField.getText().trim());
                if (recipientCard.isEmpty() || !recipientCard.matches("\\d+")) {
                    JOptionPane.showMessageDialog(this, "Invalid recipient card number!");
                    return;
                }
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be positive!");
                    return;
                }
                if (amount > currentUser.getBalance()) {
                    JOptionPane.showMessageDialog(this, "Insufficient funds!");
                    return;
                }
                if (recipientCard.equals(currentUser.getCardNumber())) {
                    JOptionPane.showMessageDialog(this, "Cannot transfer to your own card!");
                    return;
                }
                User recipient = userDAO.findUserByCardNumber(recipientCard);
                if (recipient == null) {
                    JOptionPane.showMessageDialog(this, "Recipient not found!");
                    return;
                }
                currentUser.setBalance(currentUser.getBalance() - amount);
                recipient.setBalance(recipient.getBalance() + amount);
                userDAO.updateBalance(currentUser.getUserId(), currentUser.getBalance());
                userDAO.updateBalance(recipient.getUserId(), recipient.getBalance());
                JOptionPane.showMessageDialog(this, "Transfer successful!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid amount!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Database error!\n" + e.getMessage());
            }
        }
    }

    private void printPassbook() {
        JOptionPane.showMessageDialog(this, "Passbook printing...\n" +
            "Name: " + currentUser.getName() + "\n" +
            "Card Number: " + currentUser.getCardNumber() + "\n" +
            "Balance: ₹" + currentUser.getBalance());
    }

    private void printReceipt() {
        String receipt = "Smart ATM Receipt\n" +
            "Date: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\n" +
            "Name: " + currentUser.getName() + "\n" +
            "Card Number: " + currentUser.getCardNumber() + "\n" +
            "Balance: ₹" + currentUser.getBalance();
        receiptArea = new JTextArea(receipt, 10, 30);
        receiptArea.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(receiptArea), "Receipt", JOptionPane.INFORMATION_MESSAGE);
    }

    private void cancelTransaction() {
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "welcome");
        currentUser = null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ATMSimulatorUI().setVisible(true));
    }
}