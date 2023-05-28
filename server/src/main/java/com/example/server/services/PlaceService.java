package com.example.server.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.server.models.Place;
import com.example.server.repositories.APlinkRepo;
import com.example.server.repositories.PlaceRepo;

@Service
public class PlaceService {

    @Autowired
    GoogleApiService gApiSvc;

    @Autowired
    PlaceRepo pRepo;

    @Autowired
    APlinkRepo apRepo;

    public void savePlacesForCollection(String accountId, List<Place> places) {

        for (Place p : places) {
            // check if place has been saved before
            if (!pRepo.findById(p.getPlaceId()).isEmpty()) {
                // Do nothing
                System.out.println(p.getName() + " already exists");
            } else {
                // Save p if p was not saved in MySQL before
                pRepo.save(p);

            }
        }

    }

    public List<Place> getPlacesForCollection(String accountId){
        return pRepo.findByAccountId(accountId);
    }


}
