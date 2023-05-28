package com.example.server.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.server.models.Review;
import com.example.server.repositories.ReviewRepo;

@Service
public class ReviewService {
    
    @Autowired
    ReviewRepo reviewRepo;

    public Review addReview(Review review) {
       return reviewRepo.insertReview(review);
    }

    public List<Review> getAllReviews(String linkId){
        return reviewRepo.getAllComments(linkId).stream()
                .map(r -> Review.create(r))
                .toList();
    }

    public void deleteAllReviews(String linkId){
        reviewRepo.deleteReviews(linkId);
        System.out.println("deleting comments for >>> " + linkId);
    }

}
