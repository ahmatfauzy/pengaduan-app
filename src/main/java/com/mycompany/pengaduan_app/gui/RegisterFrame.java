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
public class RegisterFrame extends JFrame {
    private JTextField usernameField, emailField, namaField;
    private JPasswordField passwordField, confirmPasswordField;
    private AuthService authService;
    private ResourceBundle messages;
    
    public RegisterFrame() {
        authService = new AuthService();
        messages = ResourceBundle.getBundle("messages", Locale.getDefault());
        initComponents();
    }
    
    private void initComponents() {
        setTitle(messages.getString("register.title"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        setSize(400, 400);
        setLocationRelativeTo(null);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Fields
        String[] labels = {"register.username", "register.email", "register.nama", 
                          "register.password", "register.confirm"};
        JComponent[] fields = {
            usernameField = new JTextField(15),
            emailField = new JTextField(15),
            namaField = new JTextField(15),
            passwordField = new JPasswordField(15),
            confirmPasswordField = new JPasswordField(15)
        };
        
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            add(new JLabel(messages.getString(labels[i])), gbc);
            
            gbc.gridx = 1;
            add(fields[i], gbc);
        }
        
        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton registerButton = new JButton(messages.getString("register.button"));
        JButton cancelButton = new JButton(messages.getString("cancel.button"));
        
        registerButton.addActionListener(this::registerAction);
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = labels.length; gbc.gridwidth = 2;
        add(buttonPanel, gbc);
    }
    
    private void registerAction(ActionEvent e) {
        String username = usernameField.getText();
        String email = emailField.getText();
        String nama = namaField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        if (username.isEmpty() || email.isEmpty() || nama.isEmpty() || 
            password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, messages.getString("register.empty"));
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, messages.getString("register.password.mismatch"));
            return;
        }
        
        User user = new User(username, password, email, nama, "USER");
        
        ThreadUtil.runAsync(() -> authService.register(user))
                .thenAccept(success -> SwingUtilities.invokeLater(() -> {
                    if (success) {
                        JOptionPane.showMessageDialog(this, messages.getString("register.success"));
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, messages.getString("register.failed"));
                    }
                }));
    }
}
