package com.example.server.JWT;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.models.Account;
import com.example.server.repositories.AccountRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccountDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepo accRepo;

    private Account userDetail;

    @Override
    @Transactional(rollbackFor = UsernameNotFoundException.class)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(">>>> Inside loadUserByUsername: {} ", username);

        Account acc = new Account();
        if (!accRepo.findByEmail(username).isEmpty()) {
            acc = accRepo.findByEmail(username).get(0);

            return new org.springframework.security.core.userdetails.User(
                    acc.getEmail(),
                    acc.getPassword(),
                    new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found.");
        }

    }

    public Account getUserDetail(String username) {
        userDetail = accRepo.findByEmail(username).get(0); 
        log.info("Inside getUserDetail: {}", userDetail);

        return userDetail;
    }

}
