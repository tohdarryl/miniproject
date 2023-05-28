package com.example.server.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.server.models.APlink;
import com.example.server.models.Place;
import com.example.server.repositories.APlinkRepo;

@Service
public class APlinkService {

    @Autowired
    APlinkRepo apRepo;
    
    public void saveLinksForCollection(String accountId, List<Place> places) {
        for (Place p : places) {
            // check if link has been saved before 
                
                if (!apRepo.findByLink(accountId, p.getPlaceId()).isEmpty()) {
                    System.out.println(accountId+"+"+p.getPlaceId()+" already exists");
                } else {
                    // Create linkId
                    String linkId = accountId + "+" + p.getPlaceId();
                    // Build APlink
                    APlink link = APlink.builder()
                            .id(linkId)
                            .accountId(accountId)
                            .placeId(p.getPlaceId())
                            .build();
                    // Save APlink
                    apRepo.save(link);

                }
        }

        
    }

    public int deleteFromCollection(String id){
        return apRepo.deleteById(id);
    }
}
