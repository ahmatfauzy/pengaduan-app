/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pengaduan_app;

import com.mycompany.pengaduan_app.gui.LoginFrame;
import java.util.Locale;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mycompany.pengaduan_app.util.LocalDateTimeAdapter;
import java.time.LocalDateTime;

/**
 *
 * @author FAUZI
 */
public class Main {
    public static void main(String[] args) {
        Locale.setDefault(new Locale("id", "ID"));
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
        
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

        LocalDateTime now = LocalDateTime.now();

        // Serialization: Mengonversi LocalDateTime menjadi JSON
        String json = gson.toJson(now);
        System.out.println("Serialized LocalDateTime: " + json);

        // Deserialization: Mengonversi JSON kembali ke LocalDateTime
        LocalDateTime deserialized = gson.fromJson(json, LocalDateTime.class);
        System.out.println("Deserialized LocalDateTime: " + deserialized);
    }
}
