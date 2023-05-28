package com.example.server.repositories;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.server.models.APlink;

@Repository
public class APlinkRepo {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private final String insertSQL = "insert into aplink(id, account_id, place_id) " +
            "values(?, ?, ?)";

    private final String findByLinkSQL = "select * from aplink where account_id = ? and place_id = ?";

    private final String deleteById = "delete from aplink where id = ?";

    public List<APlink> findByLink(String accountId, String placeId){
        List<APlink> linkList = new ArrayList<>();
        linkList = jdbcTemplate.query(findByLinkSQL, BeanPropertyRowMapper.newInstance(APlink.class), accountId, placeId);
        return linkList;
    }

    public int deleteById(String id){
        int deleted;
        deleted = jdbcTemplate.update(deleteById, id);
        return deleted;
    }
    
    public Boolean save(APlink link) {

        Integer result = jdbcTemplate.update(insertSQL, link.getId(), link.getAccountId(), link.getPlaceId());
        // if result > 0, true. Else false.
        return result > 0 ? true : false;
    }

    public int[] batchInsert(List<APlink> links) {
        return jdbcTemplate.batchUpdate(insertSQL, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, links.get(i).getAccountId());
                ps.setString(2, links.get(i).getPlaceId());
            }

            @Override
            public int getBatchSize() {
                return links.size();
            }

        });

    }
}
