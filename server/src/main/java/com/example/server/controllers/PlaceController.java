package com.example.server.controllers;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.server.models.Place;
import com.example.server.models.Review;
import com.example.server.services.APlinkService;
import com.example.server.services.AccountService;
import com.example.server.services.GoogleApiService;
import com.example.server.services.PlaceService;
import com.example.server.services.ReviewService;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;


@Controller
@CrossOrigin(origins = "*")
@RequestMapping(path = "/place", produces = MediaType.APPLICATION_JSON_VALUE)
public class PlaceController {

    @Autowired
    GoogleApiService gApiSvc;

    @Autowired
    AccountService accSvc;

    @Autowired
    PlaceService pSvc;
    
    @Autowired
    APlinkService apSvc;

    @Autowired
    ReviewService rSvc;

    @Value("${google.api.key}")
    private String GOOGLE_API_KEY;

    @GetMapping(path = "/apiKey")
    public ResponseEntity<String> getApiKey(){
        System.out.println(GOOGLE_API_KEY);

        JsonObject jsonObj = Json.createObjectBuilder()
                            .add("apiKey", GOOGLE_API_KEY)
                            .build();

        return ResponseEntity
        .status(HttpStatus.OK)
        .contentType(MediaType.APPLICATION_JSON)
        .body(jsonObj.toString());
    }

    @GetMapping(path = "/api/search")
    @ResponseBody
    public ResponseEntity<String> getPlaces(
        // Variable name 'input' must be the same as Param name of Client
            @RequestParam(required = true) String input){
        JsonArray result = null;
        // Get list of places using API
        System.out.println("Query >>> "+input);

        List<Place> listOfP = this.gApiSvc.searchPlaces(input.replaceAll("\\s",""));
        // List<Place> listOfP = this.gApiSvc.searchPlaces(input);
        System.out.println(listOfP);


        // Create JsonArray for ResponseBody
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        for (Place p : listOfP) {
            // Get website
            String website = this.gApiSvc.searchWebsite(p.getPlaceId());
            System.out.println(website);
            p.setWebsite(website);

            if(!p.getPhotoReference().equalsIgnoreCase("empty")){
            String photo = this.gApiSvc.searchPhoto(p.getPhotoReference());
            System.out.println(photo);
            p.setPhoto(photo);

            arrBuilder.add(p.toJSON());
            } else {
                p.setPhoto("/placeholder.jpg");
                arrBuilder.add(p.toJSON());
            }

        }
        result = arrBuilder.build();
        System.out.println(result);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result.toString());
    }

    @PostMapping("/collection/{accountId}")
    public ResponseEntity<String> savePlacesForCollection(@RequestBody List<Place> plist,
    @PathVariable(required = true) String accountId){
        System.out.println("AccountId: "+accountId);
        System.out.println("Places to be saved: "+plist);

        pSvc.savePlacesForCollection(accountId, plist);
        apSvc.saveLinksForCollection(accountId, plist);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/collection/{accountId}")
    public ResponseEntity<String> getPlacesForCollection( @PathVariable(required = true) String accountId){
        System.out.println("AccountId: "+accountId);
        List<Place> pList = pSvc.getPlacesForCollection(accountId);
        System.out.println(pList);

        // Create JsonArray for ResponseBody
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        for (Place p : pList) {
            arrBuilder.add(p.toJSON());
        }
        JsonArray result = null;
        result = arrBuilder.build();
        System.out.println("result >>> "+result);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(result.toString());
    }

    @DeleteMapping("/delete/{linkId}")
    public ResponseEntity<String> deleteFromCollection(@PathVariable(required = true) String linkId){
        // Variable name 'input' must be the same as Param name of Client; must be 'linkId' NOT 'LinkId'
        System.out.println("LinkId >>> "+linkId);
        apSvc.deleteFromCollection(linkId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path="/{linkId}")
    public ResponseEntity<String> saveReview(@RequestBody Review r, @PathVariable(required = true) String linkId){
        System.out.println("save comment > : " + linkId);
        Review review = new Review();
        review.setLinkId(linkId);
        review.setComment(r.getComment());
        System.out.println(r.getComment());
        // DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"); 
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");  
        review.setPosted(LocalDateTime.now().format(format));
        Review result = this.rSvc.addReview(review);

        return ResponseEntity
        .status(HttpStatus.OK)
        .contentType(MediaType.APPLICATION_JSON)
        .body(result.toJSON().toString());
    }

    @GetMapping(path = "/reviews/{linkId}")
    public ResponseEntity<String> getReview(@PathVariable(required = true) String linkId){
        System.out.println("Get All ...comments");
        List<Review> listOfR = this.rSvc.getAllReviews(linkId);

        // Build JsonArray for response
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        for (Review r : listOfR)
            arrBuilder.add(r.toJSON());
        JsonArray result = arrBuilder.build();

        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(result.toString());
    }

    @DeleteMapping("/delete/reviews/{linkId}")
    public ResponseEntity<String> deleteReviews(@PathVariable(required = true) String linkId){
        // Variable name 'input' must be the same as Param name of Client; must be 'linkId' NOT 'LinkId'
        System.out.println("LinkId >>> "+linkId);
        rSvc.deleteAllReviews(linkId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

 


   
}
