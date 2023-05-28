package com.example.server.services;

import java.util.List;
import java.util.Objects;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.server.JWT.AccountDetailsService;
import com.example.server.JWT.JwtService;
import com.example.server.models.Account;
import com.example.server.models.Role;
import com.example.server.repositories.AccountRepo;
import com.example.server.utils.EmailUtils;
import com.example.server.utils.PlaceUtils;


import jakarta.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccountService {
    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    AccountRepo accRepo;

    @Autowired
    PlaceUtils placeUtils;

    @Autowired
    EmailUtils emailUtils;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AccountDetailsService accountDetailsService;

    @Autowired
    JwtService jwtService;

    public Boolean save(Account acc) {
        Boolean saved = false;
        String emailToSearch = acc.getEmail();
        System.out.println(emailToSearch);
        String emailFromDB;

        // If email cant be found in MySql, save.
        if (accRepo.findByEmail(emailToSearch).isEmpty()) {
            emailFromDB = "";
            String accountId = UUID.randomUUID().toString().substring(0, 8);
            acc.setAccountId(accountId);

            // encode password
            acc.setPassword(passwordEncoder.encode(acc.getPassword()));

            saved = accRepo.save(acc);

        } else {
            emailFromDB = accRepo.findByEmail(emailToSearch).get(0).getEmail();
        }

        System.out.println(emailFromDB);

        return saved;

    }

    public List<Account> findByEmail(String email) {
        return accRepo.findByEmail(email);
    }

    public Account findByCred(String email, String password) {
        return accRepo.findByCred(email, password);
    }

    public List<Account> findByName(String firstName, String lastName) {
        return accRepo.findByName(firstName, lastName);
    }

    public Boolean saveUser(Account acc) {
        Boolean saved = false;

        if (!accRepo.findByEmail(acc.getEmail()).isEmpty()) {
            saved = false;
        } else {

            String accountId = UUID.randomUUID().toString().substring(0, 8);
            acc.setAccountId(accountId);
            // acc.setEmail(acc.getEmail());
            acc.setPassword(passwordEncoder.encode(acc.getPassword()));
            // acc.setFirstName(acc.getFirstName());
            // acc.setLastName(acc.getLastName());

            System.out.println(Role.USER);
            acc.setRole(Role.USER);

            saved = accRepo.save(acc);
        }
        return saved;
    }

    public ResponseEntity<String> login(String payload) {
        log.info(">>>> inside login {}", payload);

        try {
            JsonObject obj = placeUtils.jsonStringToJsonObj(payload);
            // from the payload get the email and password for authentication
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            obj.getString("email"), obj.getString("password")));

            log.info(">>>> inside login , auth: {} ", auth);

            if (auth.isAuthenticated()) {
                // if user is authenticated, generate token and return in resp
                return new ResponseEntity<String>("{\"token\":\"" +
                        jwtService.generateToken(
                                accountDetailsService.getUserDetail(obj.getString("email")).getEmail(),
                                accountDetailsService.getUserDetail(obj.getString("email")).getRole(),
                                accountDetailsService.getUserDetail(obj.getString("email")).getFirstName(),
                                accountDetailsService.getUserDetail(obj.getString("email")).getLastName(),
                                accountDetailsService.getUserDetail(obj.getString("email")).getAccountId())
                        + "\"}",
                        HttpStatus.OK);

            }
        } catch (Exception e) {
            log.error("{}", e);
        }
        return new ResponseEntity<String>("{\"message\":\"" +
                "Bad Credentials." + "\"}",
                HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> checkToken() {
     return placeUtils.getResponseEntity("true", HttpStatus.OK);
    }

    public ResponseEntity<String> changePassword(String email, String oldPassword, String newPassword){
        try {  
        Account acc = accRepo.findByEmail(email).get(0);
        System.out.println(">>> account to change password"+acc);

        if(acc != null){
            System.out.println("Existing Password in Database : "+ acc.getPassword());
            System.out.println("Old Password Inputted : "+ oldPassword);
            if(passwordEncoder.matches(oldPassword, acc.getPassword())){
                acc.setPassword(passwordEncoder.encode(newPassword));
                accRepo.updatePassword(acc);
               return placeUtils.getResponseEntity("Password Updated Successfully", HttpStatus.OK);
            } else {
            return placeUtils.getResponseEntity("Incorrect Old Password", HttpStatus.BAD_REQUEST);
            }
        }
        // if can't find user
        return placeUtils.getResponseEntity("Cannot Find User", HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return placeUtils.getResponseEntity("", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> forgotPassword(String email) {
        try {
            Account acc = accRepo.findByEmail(email).get(0);
            System.out.println(">>> account to reset password : "+acc);
            if(!Objects.isNull(acc)) {
                // reset password to random UUID
                String resetPassword = UUID.randomUUID().toString().substring(0, 8);
                System.out.println(">>> resetPassword "+resetPassword);
                acc.setPassword(passwordEncoder.encode(resetPassword));
                accRepo.updatePassword(acc);
                // setting email config 
                emailUtils.forgotMail(acc.getEmail(), "Credentials by Beento", resetPassword);
                return placeUtils.getResponseEntity("Check your mail for Credentials", HttpStatus.OK);
            }
            

        } catch (Exception e) {
            e.printStackTrace();
        }
        return placeUtils.getResponseEntity("", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
