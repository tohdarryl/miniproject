package com.example.server.models;


import org.bson.Document;
import org.bson.types.Binary;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    
    private String linkId;
    private String comment;
    private Binary image;
    private String posted;
    
    // Java Object to Document
    public Document toDocument() {
        Document doc = new Document();
        doc.put("linkId", getLinkId());
        doc.put("comment", getComment());
        // doc.put("image", getImage());
        doc.put("posted", getPosted());
        return doc;
    }

    // Document -> Java Object
    public static Review create(Document d) {
        Review r = new Review();
        r.setLinkId(d.getString("linkId"));
        r.setComment(d.getString("comment"));
        // LocalDateTime postedDt = Instant.ofEpochMilli(d.getDate("posted").getTime())
        // .atZone(ZoneId.systemDefault())
        // .toLocalDateTime();
        r.setPosted(d.getString("posted"));
        return r;
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("linkId", getLinkId())
                .add("comment", getComment())
                .add("posted", getPosted().toString())
                .build();
    }

}
