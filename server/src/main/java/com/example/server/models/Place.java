package com.example.server.models;



import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Place {

    private String address;
    private String name;
    private String placeId;
    private Double rating;
    private int totalUsersRated;
    private String icon;
    private String iconColour;
    private String photoReference;
    private String photo;
    private String website;
    private BigDecimal latitude;
    private BigDecimal longtitude;
    private List<APlink> listOfAcc = new LinkedList<>();
    


    public static Place toPlace(JsonObject obj){
        Place p = new Place();
        p.setAddress(obj.getString("formatted_address"));
        p.setName(obj.getString("name"));
        p.setPlaceId(obj.getString("place_id"));
        p.setRating(obj.getJsonNumber("rating").doubleValue());
        p.setTotalUsersRated(obj.getInt("user_ratings_total"));
        p.setIcon(obj.getString("icon"));
        p.setIconColour(obj.getString("icon_background_color"));
        
        // lat and lang within location thats within geometry
        JsonObject geometry = obj.getJsonObject("geometry");
        JsonObject location = geometry.getJsonObject("location");
        p.setLatitude(location.getJsonNumber("lat").bigDecimalValue());
        p.setLongtitude(location.getJsonNumber("lng").bigDecimalValue());

        // photos is in JsonArray
        if(obj.getJsonArray("photos") != null){
        JsonArray photos = obj.getJsonArray("photos");
        for (int i = 0; i < photos.size(); i++) {
            JsonObject x = photos.getJsonObject(i);
            p.setPhotoReference(x.getString("photo_reference"));
        }
        } else {
            p.setPhotoReference("empty");
        }
        
       
               
	    
      
        return p;
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("address", getAddress())
                .add("name", getName())
                .add("placeId", getPlaceId())
                .add("rating", getRating())
                .add("totalUsersRated", getTotalUsersRated())
                .add("icon", getIcon())
                .add("iconColour", getIconColour())
                .add("photoReference", getPhotoReference())
                .add("latitude", getLatitude())
                .add("longtitude", getLongtitude())
                .add("photo", getPhoto())
                .add("website", getWebsite())
                .build();
    }

}
