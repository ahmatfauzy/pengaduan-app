/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pengaduan_app.gui;

import com.mycompany.pengaduan_app.model.User;
import com.mycompany.pengaduan_app.service.AuthService;
import com.mycompany.pengaduan_app.util.ThreadUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author FAUZI
 */
public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private AuthService authService;
    private ResourceBundle messages;
    
    public LoginFrame() {
        authService = new AuthService();
        messages = ResourceBundle.getBundle("messages", Locale.getDefault());
        initComponents();
    }
    
    private void initComponents() {
        setTitle(messages.getString("login.title"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        setSize(400, 300);
        setLocationRelativeTo(null);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Title
        JLabel titleLabel = new JLabel(messages.getString("app.title"));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);
        
        // Username
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel(messages.getString("login.username")), gbc);
        
        usernameField = new JTextField(15);
        gbc.gridx = 1;
        add(usernameField, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel(messages.getString("login.password")), gbc);
        
        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        add(passwordField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton loginButton = new JButton(messages.getString("login.button"));
        JButton registerButton = new JButton(messages.getString("register.button"));
        
        loginButton.addActionListener(this::loginAction);
        registerButton.addActionListener(this::registerAction);
        
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(buttonPanel, gbc);
        
        // Language selector
        JPanel langPanel = new JPanel();
        JButton idButton = new JButton("ID");
        JButton enButton = new JButton("EN");
        
        idButton.addActionListener(e -> changeLanguage(new Locale("id", "ID")));
        enButton.addActionListener(e -> changeLanguage(Locale.ENGLISH));
        
        langPanel.add(idButton);
        langPanel.add(enButton);
        
        gbc.gridy = 4;
        add(langPanel, gbc);
    }
    
    private void loginAction(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, messages.getString("login.empty"));
            return;
        }
        
        ThreadUtil.runAsync(() -> authService.login(username, password))
                .thenAccept(user -> SwingUtilities.invokeLater(() -> {
                    if (user != null) {
                        dispose();
                        if ("ADMIN".equals(user.getRole())) {
                            new AdminDashboard(user).setVisible(true);
                        } else {
                            new UserDashboard(user).setVisible(true);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, messages.getString("login.failed"));
                    }
                }));
    }
    
    private void registerAction(ActionEvent e) {
        new RegisterFrame().setVisible(true);
    }
    
    private void changeLanguage(Locale locale) {
        Locale.setDefault(locale);
        dispose();
        new LoginFrame().setVisible(true);
    }
}
