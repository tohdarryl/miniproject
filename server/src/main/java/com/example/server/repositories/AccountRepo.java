package com.example.server.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Repository;

import com.example.server.models.Account;

@Repository
public class AccountRepo {
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    private final String findAllSQL = "select * from account";

    private final String findByEmailSQL = "select * from account where email = ?";

    private final String findByCredSQL = "SELECT * FROM account WHERE email = ? AND password = ?";
    // Must be '%' ? '%' for like
    private final String findByNameSQL = "select * from account where first_name like '%' ? '%' and last_name like '%' ? '%'";

    private final String insertSQL = "insert into account(account_id, email, password, first_name, last_name, role) " +
    "values(?, ?, ?, ?, ?, ?)";

    private final String updateSQL = "update account set password = ? " +
    "where email = ?";

   


    public List<Account> findAll(){
        // .query returns a list of Account objects
        // .queryForObject returns 1 Account object
        // BeanPropertyRowMapper provides auto-mapping
        List<Account> aList = new ArrayList<>();
        aList = jdbcTemplate.query(findAllSQL, BeanPropertyRowMapper.newInstance(Account.class));
        return aList;
    }

    public List<Account> findByEmail(String email){
        List<Account> aList = new ArrayList<>();
        aList = jdbcTemplate.query(findByEmailSQL, BeanPropertyRowMapper.newInstance(Account.class), email);
        return aList;
    }

    public Account findByCred(String email, String password){
        Account acc = new Account();
        acc = jdbcTemplate.queryForObject(findByCredSQL, BeanPropertyRowMapper.newInstance(Account.class), email, password);
        return acc;
    }
    
    
    public List<Account> findByName(String firstName, String lastName){
        List<Account> aList = new ArrayList<>();
        aList = jdbcTemplate.query(findByNameSQL,
        BeanPropertyRowMapper.newInstance(Account.class), firstName, lastName);
        return aList;
    }

    public Boolean save(Account acc){
    
        Integer result = jdbcTemplate.update(insertSQL, acc.getAccountId(), acc.getEmail(), acc.getPassword(), acc.getFirstName(),
        acc.getLastName(), acc.getRole().name());
        // if result > 0, true. Else false.
        return result>0?true:false;
    }

    public Boolean updatePassword(Account acc){
    
        Integer result = jdbcTemplate.update(updateSQL, acc.getPassword(), acc.getEmail());
        // if result > 0, true. Else false.
        return result>0?true:false;
    }
}
