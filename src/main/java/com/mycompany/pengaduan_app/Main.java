/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pengaduan_app;

import com.mycompany.pengaduan_app.gui.LoginFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import com.mycompany.pengaduan_app.NewClass;

/**
 *
 * @author FAUZI
 */
public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        NewClass.main(args);
        
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
