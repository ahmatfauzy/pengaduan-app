/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pengaduan_app.gui;

import com.mycompany.pengaduan_app.model.Pengaduan;
import com.mycompany.pengaduan_app.model.User;
import com.mycompany.pengaduan_app.service.PengaduanService;
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
public class PengaduanForm extends JFrame {
    private User currentUser;
    private UserDashboard parentDashboard;
    private PengaduanService pengaduanService;
    private ResourceBundle messages;
    private JTextField judulField;
    private JComboBox<String> kategoriCombo;
    private JTextArea deskripsiArea;
    
    public PengaduanForm(User user, UserDashboard parent) {
        this.currentUser = user;
        this.parentDashboard = parent;
        this.pengaduanService = new PengaduanService();
        this.messages = ResourceBundle.getBundle("messages", Locale.getDefault());
        initComponents();
    }
    
    private void initComponents() {
        setTitle(messages.getString("pengaduan.form.title"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        setSize(500, 400);
        setLocationRelativeTo(null);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Judul
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel(messages.getString("pengaduan.judul")), gbc);
        
        judulField = new JTextField(20);
        gbc.gridx = 1;
        add(judulField, gbc);
        
        // Kategori
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel(messages.getString("pengaduan.kategori")), gbc);
        
        String[] kategoris = {"Infrastruktur", "Pelayanan Publik", "Keamanan", "Lingkungan", "Lainnya"};
        kategoriCombo = new JComboBox<>(kategoris);
        gbc.gridx = 1;
        add(kategoriCombo, gbc);
        
        // Deskripsi
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel(messages.getString("pengaduan.deskripsi")), gbc);
        
        deskripsiArea = new JTextArea(10, 20);
        deskripsiArea.setLineWrap(true);
        deskripsiArea.setWrapStyleWord(true);
        gbc.gridx = 1;
        add(new JScrollPane(deskripsiArea), gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton submitButton = new JButton(messages.getString("submit.button"));
        JButton cancelButton = new JButton(messages.getString("cancel.button"));
        
        submitButton.addActionListener(this::submitAction);
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(buttonPanel, gbc);
    }
    
    private void submitAction(ActionEvent e) {
        String judul = judulField.getText().trim();
        String kategori = (String) kategoriCombo.getSelectedItem();
        String deskripsi = deskripsiArea.getText().trim();
        
        if (judul.isEmpty() || deskripsi.isEmpty()) {
            JOptionPane.showMessageDialog(this, messages.getString("pengaduan.form.empty"));
            return;
        }
        
        Pengaduan pengaduan = new Pengaduan(currentUser.getId(), judul, deskripsi, kategori);
        
        ThreadUtil.runAsync(() -> {
            pengaduanService.createPengaduan(pengaduan);
            return null;
        }).thenRun(() -> SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, messages.getString("pengaduan.submit.success"));
            parentDashboard.refreshData();
            dispose();
        }));
    }
}
