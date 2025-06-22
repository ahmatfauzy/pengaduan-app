/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pengaduan_app.service;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mycompany.pengaduan_app.model.User;
import com.mycompany.pengaduan_app.util.CryptoUtil;
import java.util.concurrent.CompletableFuture;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author FAUZI
 */
public class AuthService {
    private MongoCollection<Document> userCollection;
    private Gson gson;
    private DatabaseService dbService;
    
    public AuthService() {
        dbService = DatabaseService.getInstance();
        userCollection = dbService.getDatabase().getCollection("users");
        gson = new Gson();
    }
    
    public User login(String username, String password) {
        // Check admin credentials
        String adminUser = dbService.getDotenv().get("ADMIN_USERNAME");
        String adminPass = dbService.getDotenv().get("ADMIN_PASSWORD");
        
        if (username.equals(adminUser) && password.equals(adminPass)) {
            User admin = new User(adminUser, "", "", "Administrator", "ADMIN");
            admin.setId("admin");
            return admin;
        }
        
        // Check user credentials
        Document userDoc = userCollection.find(Filters.eq("username", username)).first();
        if (userDoc != null) {
            User user = gson.fromJson(userDoc.toJson(), User.class);
            user.setId(userDoc.getObjectId("_id").toString());
            
            if (CryptoUtil.checkPassword(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }
    
    public boolean register(User user) {
        // Check if username exists
        if (userCollection.find(Filters.eq("username", user.getUsername())).first() != null) {
            return false;
        }
        
        // Hash password
        user.setPassword(CryptoUtil.hashPassword(user.getPassword()));
        user.setRole("USER");
        
        // Save to database
        String json = gson.toJson(user);
        Document doc = Document.parse(json);
        doc.remove("id");
        
        userCollection.insertOne(doc);
        return true;
    }

//    public CompletableFuture<Object> login(String username, String password) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
}
