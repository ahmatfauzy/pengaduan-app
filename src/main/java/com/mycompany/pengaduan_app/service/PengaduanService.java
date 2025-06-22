/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pengaduan_app.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mycompany.pengaduan_app.model.Pengaduan;
import com.mycompany.pengaduan_app.util.LocalDateTimeAdapter;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author FAUZI
 */
public class PengaduanService {
    private MongoCollection<Document> pengaduanCollection;
    private Gson gson;
    
    public PengaduanService() {
        DatabaseService dbService = DatabaseService.getInstance();
        pengaduanCollection = dbService.getDatabase().getCollection("pengaduan");
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }
    
    public void createPengaduan(Pengaduan pengaduan) {
        String json = gson.toJson(pengaduan);
        Document doc = Document.parse(json);
        doc.remove("id");
        pengaduanCollection.insertOne(doc);
    }
    
    public List<Pengaduan> getPengaduanByUser(String userId) {
        List<Pengaduan> result = new ArrayList<>();
        for (Document doc : pengaduanCollection.find(Filters.eq("userId", userId))) {
            Pengaduan pengaduan = gson.fromJson(doc.toJson(), Pengaduan.class);
            pengaduan.setId(doc.getObjectId("_id").toString());
            result.add(pengaduan);
        }
        return result;
    }
    
    public List<Pengaduan> getAllPengaduan() {
        List<Pengaduan> result = new ArrayList<>();
        for (Document doc : pengaduanCollection.find()) {
            Pengaduan pengaduan = gson.fromJson(doc.toJson(), Pengaduan.class);
            pengaduan.setId(doc.getObjectId("_id").toString());
            result.add(pengaduan);
        }
        return result;
    }
    
    public void updateStatus(String id, String status, String response) {
        pengaduanCollection.updateOne(
            Filters.eq("_id", new ObjectId(id)),
            new Document("$set", new Document("status", status)
                .append("response", response)
                .append("tanggalUpdate", LocalDateTime.now().toString()))
        );
    }
}
