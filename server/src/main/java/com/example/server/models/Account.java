package com.example.server.models;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Account implements UserDetails{

    
    private String accountId;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private List<APlink> listOfPlaces = new LinkedList<>();
	

    @Enumerated(EnumType.STRING)
    private Role role;

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("accountId", getAccountId())
                .add("email", getEmail())
                .add("password", getPassword())
                .add("firstName", getFirstName())
                .add("lastName", getLastName())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

   
}
