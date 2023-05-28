package com.example.server.JWT;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.server.models.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JwtService {
	@Value("${jwt.secret}")
	private String JWT_SECRET;

	@Value("${jwt.token.validity}")
	private long tokenValidity;

	// extract username from jwt
	public String extractUserName(String token) {
		try {
			// Signing key is a secret used to create the signature part of the jwt, to
			// verify the client
			
			// Claims body =
			// Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
			// return body.getSubject();

			return extractClaim(token, Claims::getSubject);

		} catch (Exception e) {
			System.out.println(e.getMessage() + " => " + e);
		}

		return null;
	}

	// methods to extract data from JSON in the JWT body.
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSignInKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	// create and generate token from username (the username here is the email)
	public String generateToken(String username, Role role, String firstName, String lastName, String accountId) {
		Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
		claims.put("firstName", firstName);
		claims.put("lastName", lastName);
		claims.put("accountId", accountId);

		return generateToken(claims, username);
	}

	public String generateToken(
			Map<String, Object> claims,
			String username) {
		log.info(">>>> Inside createToken - claims & username: {} : {} ", claims, username);
		return Jwts
				.builder()
				.setClaims(claims)
				.setSubject(username)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + tokenValidity))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256)
				.compact();
	}


	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUserName(token);
		// if username is valid and token is expired
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}

	public boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

}
