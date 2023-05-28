package com.example.server.repositories;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.server.models.Place;

@Repository
public class PlaceRepo {
    
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    private final String insertSQL = "insert into place(place_id, name, address, rating, total_users_rated, icon, icon_colour, photo_reference, photo, latitude, longtitude, website) " +
    "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private final String findByIdSQL = "select * from place where place_id = ?";

    private final String findPlacesByAccountId = 
    "select p.place_id, p.name, p.address, p.rating, p.total_users_rated, p.icon, p.icon_colour, p.photo_reference, p.photo, p.latitude, p.longtitude, p.website " +
    "from account a " +
    "left join " +  
    "aplink link " +
    "on a.account_id = link.account_id " +
    "left join " +
    "place p " +
    "on link.place_id = p.place_id " +
    "where a.account_id = ? " +
    "order by p.name ";

    public List<Place> findById(String id){
        List<Place> pList = new ArrayList<>();
        pList = jdbcTemplate.query(findByIdSQL,
        BeanPropertyRowMapper.newInstance(Place.class), id);
        return pList;
    }

    public List<Place> findByAccountId(String accountId){
        List<Place> pList = new ArrayList<>();
        pList = jdbcTemplate.query(findPlacesByAccountId,
        BeanPropertyRowMapper.newInstance(Place.class), accountId);
        return pList;
    }


    public Boolean save(Place p){
    
        Integer result = jdbcTemplate.update(insertSQL, p.getPlaceId(), p.getName(), p.getAddress(), p.getRating(), p.getTotalUsersRated(),
        p.getIcon(), p.getIconColour(), p.getPhotoReference(), p.getPhoto(), p.getLatitude(), p.getLongtitude(), p.getWebsite());
        // if result > 0, true. Else false.
        return result>0?true:false; 
    }

        public int[] batchInsert(List<Place> places){
        return jdbcTemplate.batchUpdate(insertSQL, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, places.get(i).getPlaceId());
                ps.setString(2, places.get(i).getName());
                ps.setString(3, places.get(i).getAddress());
                ps.setDouble(4, places.get(i).getRating());
                ps.setInt(5, places.get(i).getTotalUsersRated());
                ps.setString(6, places.get(i).getIcon());
                ps.setString(7, places.get(i).getIconColour());
                ps.setString(8, places.get(i).getPhoto());
                ps.setString(9, places.get(i).getPhotoReference());
            }

            @Override
            public int getBatchSize() {
                return places.size();
            }
            
        });

    }
}
