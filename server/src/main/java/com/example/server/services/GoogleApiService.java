package com.example.server.services;

import java.io.StringReader;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.server.models.Place;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue.ValueType;

@Service
public class GoogleApiService {

    @Value("${google.api.key}")
    private String GOOGLE_API_KEY;

    private String fields = "formatted_address,icon,icon_background_color,place_id,photos,name,rating,user_ratings_total,opening_hours,geometry";
    private String inputtype = "textquery";

    public static final String GOOGLE_PLACES_API = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json";
    public static final String GOOGLE_PLACES_API2 = "https://maps.googleapis.com/maps/api/place/textsearch/json";
    public static final String GOOGLE_PHOTO_API = "https://maps.googleapis.com/maps/api/place/photo";
    public static final String GOOGLE_PHOTO_DETAILS_API = "https://maps.googleapis.com/maps/api/place/details/json";

    public List<Place> searchPlaces(String input) {
        String url = UriComponentsBuilder.fromUriString(GOOGLE_PLACES_API2)
                // .queryParam("fields", fields)
                // .queryParam("input", input)
                .queryParam("query", input+"singapore")
                // .queryParam("inputtype", inputtype)
                .queryParam("key", GOOGLE_API_KEY)
                .toUriString();

        RequestEntity<Void> req = RequestEntity.get(url)
                .accept(MediaType.APPLICATION_JSON)
                .build();

        // resttemplate to get info from api
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = null;

        try {
            // exchange: sends request to api, in return for response
            resp = template.exchange(req, String.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            // return empty list if any errors
            return Collections.emptyList();
        }

        String payload = resp.getBody();
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject placeResp = reader.readObject();
        JsonArray jsonArr = placeResp.getJsonArray("results");

        System.out.println(jsonArr);

        // For any values = null
        if (jsonArr == null || jsonArr.getValueType() == ValueType.NULL) {
            return Collections.emptyList();
        } else {

            return jsonArr.stream()
                    .map(v -> v.asJsonObject())
                    .map(Place::toPlace)
                    .toList();
        }

    }

    // Get photos
    public String searchPhoto(String photoRef){
        String url = UriComponentsBuilder.fromUriString(GOOGLE_PHOTO_API)
                .queryParam("photo_reference", photoRef)
                .queryParam("maxwidth", "200")
                .queryParam("maxheight", "200")
                .queryParam("key", GOOGLE_API_KEY)
                .toUriString();

        RestTemplate template = new RestTemplate();

        ResponseEntity<String> resp = null;
        resp = template.getForEntity(url, String.class);

        // Cannot get ResponseHeader's location (or redirected URL)
        String location = resp.getHeaders().getFirst("location");
        System.out.println("location >>>  " + location);

        return url;

    }   

    // Get website
    private String fieldsForWebsite = "website";

    public String searchWebsite(String placeId) {
        String url = UriComponentsBuilder.fromUriString(GOOGLE_PHOTO_DETAILS_API)
                .queryParam("fields", fieldsForWebsite)
                .queryParam("place_id", placeId)
                .queryParam("key", GOOGLE_API_KEY)
                .toUriString();

        System.out.println(url);

        RequestEntity<Void> req = RequestEntity.get(url)
                .accept(MediaType.APPLICATION_JSON)
                .build();

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = null;

        try {
            // exchange: sends request to api, in return for response
            resp = template.exchange(req, String.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            // return empty list if any errors
            return "";
        }

        String payload = resp.getBody();
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject placeDetailsResp = reader.readObject();
        JsonObject jsonO = placeDetailsResp.getJsonObject("result");

        System.out.println(jsonO);

        // For any values = null
        if (jsonO == null || jsonO.getValueType() == ValueType.NULL || jsonO.isEmpty()) {
            return "";
        } else {
            String website = jsonO.getString("website");
            System.out.println(website);
            return website;

        }

    }
}
