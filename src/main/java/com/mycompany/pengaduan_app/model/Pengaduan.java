/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pengaduan_app.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author FAUZI
 */
public class Pengaduan implements Serializable {
    private String id;
    private String userId;
    private String judul;
    private String deskripsi;
    private String kategori;
    private String status; // "BELUM", "PROSES", "SELESAI"
    private LocalDateTime tanggalBuat;
    private LocalDateTime tanggalUpdate;
    private String response;
    
    public Pengaduan() {
        this.tanggalBuat = LocalDateTime.now();
        this.status = "BELUM";
    }
    
    public Pengaduan(String userId, String judul, String deskripsi, String kategori) {
        this();
        this.userId = userId;
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.kategori = kategori;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }
    
    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
    
    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { 
        this.status = status;
        this.tanggalUpdate = LocalDateTime.now();
    }
    
    public LocalDateTime getTanggalBuat() { return tanggalBuat; }
    public void setTanggalBuat(LocalDateTime tanggalBuat) { this.tanggalBuat = tanggalBuat; }
    
    public LocalDateTime getTanggalUpdate() { return tanggalUpdate; }
    public void setTanggalUpdate(LocalDateTime tanggalUpdate) { this.tanggalUpdate = tanggalUpdate; }
    
    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }
}
