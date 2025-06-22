/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pengaduan_app.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import io.github.cdimascio.dotenv.Dotenv;

/**
 *
 * @author FAUZI
 */
public class DatabaseService {
    private static DatabaseService instance;
    private MongoClient mongoClient;
    private MongoDatabase database;
    private Dotenv dotenv;
    
    private DatabaseService() {
        dotenv = Dotenv.load();
        String uri = dotenv.get("MONGODB_URI");
        String dbName = dotenv.get("DB_NAME");
        
        mongoClient = MongoClients.create(uri);
        database = mongoClient.getDatabase(dbName);
    }
    
    public static synchronized DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }
    
    public MongoDatabase getDatabase() {
        return database;
    }
    
    public Dotenv getDotenv() {
        return dotenv;
    }
    
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
