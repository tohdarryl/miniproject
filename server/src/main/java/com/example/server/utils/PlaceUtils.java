package com.example.server.utils;

import java.io.StringReader;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;


@Component
public class PlaceUtils {

    public JsonObject jsonStringToJsonObj(String json) {
        JsonReader reader = Json.createReader(new StringReader(json));
        return reader.readObject();
    }

    public ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus){
        return new ResponseEntity<String>("{\"message\":\""+responseMessage+"\"}", httpStatus);
    }
}
