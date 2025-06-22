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
public class AdminDashboard extends JFrame {
    private User currentUser;
    private PengaduanService pengaduanService;
    private ResourceBundle messages;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Pengaduan> pengaduanList;
    
    public AdminDashboard(User user) {
        this.currentUser = user;
        this.pengaduanService = new PengaduanService();
        this.messages = ResourceBundle.getBundle("messages", Locale.getDefault());
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setTitle(messages.getString("admin.dashboard.title"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        // Top panel
        JPanel topPanel = new JPanel();
        JButton refreshButton = new JButton(messages.getString("refresh.button"));
        JButton updateStatusButton = new JButton(messages.getString("pengaduan.update.status"));
        JButton logoutButton = new JButton(messages.getString("logout.button"));
        
        refreshButton.addActionListener(e -> loadData());
        updateStatusButton.addActionListener(this::updateStatusAction);
        logoutButton.addActionListener(this::logoutAction);
        
        topPanel.add(refreshButton);
        topPanel.add(updateStatusButton);
        topPanel.add(logoutButton);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {messages.getString("pengaduan.judul"), 
                           messages.getString("pengaduan.kategori"),
                           messages.getString("pengaduan.status"),
                           messages.getString("pengaduan.tanggal"),
                           messages.getString("pengaduan.deskripsi")};
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
    
    private void loadData() {
        ThreadUtil.runAsync(() -> pengaduanService.getAllPengaduan())
                .thenAccept(list -> SwingUtilities.invokeLater(() -> {
                    this.pengaduanList = list;
                    tableModel.setRowCount(0);
                    for (Pengaduan p : list) {
                        tableModel.addRow(new Object[]{
                            p.getJudul(), p.getKategori(), p.getStatus(), 
                            p.getTanggalBuat().toString(), p.getDeskripsi()
                        });
                    }
                }));
    }
    
    private void updateStatusAction(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, messages.getString("admin.select.pengaduan"));
            return;
        }
        
        Pengaduan selected = pengaduanList.get(selectedRow);
        
        String[] statuses = {"BELUM", "PROSES", "SELESAI"};
        String newStatus = (String) JOptionPane.showInputDialog(
            this, messages.getString("pengaduan.select.status"), 
            messages.getString("pengaduan.update.status"),
            JOptionPane.QUESTION_MESSAGE, null, statuses, selected.getStatus()
        );
        
        if (newStatus != null && !newStatus.equals(selected.getStatus())) {
            String response = JOptionPane.showInputDialog(
                this, messages.getString("pengaduan.response"), ""
            );
            
            ThreadUtil.runAsync(() -> {
                pengaduanService.updateStatus(selected.getId(), newStatus, response);
                return null;
            }).thenRun(() -> SwingUtilities.invokeLater(this::loadData));
        }
    }
    
    private void logoutAction(ActionEvent e) {
        dispose();
        new LoginFrame().setVisible(true);
    }
}
