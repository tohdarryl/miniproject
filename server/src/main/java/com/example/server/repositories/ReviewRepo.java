package com.example.server.repositories;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.example.server.models.Review;

@Repository
public class ReviewRepo {

    @Autowired
    MongoTemplate template;

    public Review insertReview(Review review) {
        Document doc = review.toDocument();
        template.insert(doc, "reviews");
        return review;
    }

    // db.comments.find({ linkId : "?" });
    public List<Document> getAllComments(String linkId) {
        Criteria c = Criteria.where("linkId").is(linkId);

        // Pageable pageable = PageRequest.of(0, 10);

        Query q = Query.query(c)
                .limit(10)
                .skip(0);

        return template.find(q, Document.class, "reviews");
    }

    public void deleteReviews(String linkId) {
        Criteria c = Criteria.where("linkId").is(linkId);

        // Pageable pageable = PageRequest.of(0, 10);

        Query q = Query.query(c);

        template.remove(q, "reviews");
    }
}
