/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pengaduan_app.model;

import java.io.Serializable;

/**
 *
 * @author FAUZI
 */
public class User implements Serializable {
    private String id;
    private String username;
    private String password;
    private String email;
    private String nama;
    private String role; // "USER" or "ADMIN"
    
    public User() {}
    
    public User(String username, String password, String email, String nama, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nama = nama;
        this.role = role;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
