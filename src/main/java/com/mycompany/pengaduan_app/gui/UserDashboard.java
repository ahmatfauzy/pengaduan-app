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
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author FAUZI
 */
public class UserDashboard extends JFrame {
    private User currentUser;
    private PengaduanService pengaduanService;
    private ResourceBundle messages;
    private JTable table;
    private DefaultTableModel tableModel;
    
    public UserDashboard(User user) {
        this.currentUser = user;
        this.pengaduanService = new PengaduanService();
        this.messages = ResourceBundle.getBundle("messages", Locale.getDefault());
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setTitle(messages.getString("user.dashboard.title") + " - " + currentUser.getNama());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Top panel
        JPanel topPanel = new JPanel();
        JButton newPengaduanButton = new JButton(messages.getString("pengaduan.new"));
        JButton refreshButton = new JButton(messages.getString("refresh.button"));
        JButton logoutButton = new JButton(messages.getString("logout.button"));
        
        newPengaduanButton.addActionListener(this::newPengaduanAction);
        refreshButton.addActionListener(e -> loadData());
        logoutButton.addActionListener(this::logoutAction);
        
        topPanel.add(newPengaduanButton);
        topPanel.add(refreshButton);
        topPanel.add(logoutButton);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {messages.getString("pengaduan.judul"), 
                           messages.getString("pengaduan.kategori"),
                           messages.getString("pengaduan.status"),
                           messages.getString("pengaduan.tanggal")};
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
    
    private void loadData() {
        ThreadUtil.runAsync(() -> pengaduanService.getPengaduanByUser(currentUser.getId()))
                .thenAccept(pengaduanList -> SwingUtilities.invokeLater(() -> {
                    tableModel.setRowCount(0);
                    for (Pengaduan p : pengaduanList) {
                        tableModel.addRow(new Object[]{
                            p.getJudul(), p.getKategori(), p.getStatus(), 
                            p.getTanggalBuat().toString()
                        });
                    }
                }));
    }
    
    private void newPengaduanAction(ActionEvent e) {
        new PengaduanForm(currentUser, this).setVisible(true);
    }
    
    private void logoutAction(ActionEvent e) {
        dispose();
        new LoginFrame().setVisible(true);
    }
    
    public void refreshData() {
        loadData();
    }
}
