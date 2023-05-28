package com.example.server.JWT;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


// Ensures that security filter actions should only be performed once for a request
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{
    
    @Autowired
	private JwtService jwtSvc;

	@Autowired
	private AccountDetailsService accDetailsService;

	

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		final String authHeader = request.getHeader("Authorization");
		final String token;
		final String userEmail;
		

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		// '7' because Bearer has 7 chars including space
		// same as authHeader.substring("Bearer ".length())
		token = authHeader.split(" ")[1].trim();
		System.out.println("token: " + token);

		// extract Username
		userEmail = jwtSvc.extractUserName(token);
		System.out.println("extracted token user: "+ userEmail);



		if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
			UserDetails userDetails = this.accDetailsService.loadUserByUsername(userEmail);
			if (jwtSvc.isTokenValid(token, userDetails)){
				// instantiate authToken
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken
				(userDetails, null, userDetails.getAuthorities());
				authToken.setDetails(
					new WebAuthenticationDetailsSource().buildDetails(request)
				);
				SecurityContextHolder.getContext().setAuthentication(authToken);

				log.info(">>>> Inside doFilterInternal - authToken : {}", authToken);
			}

		}

		filterChain.doFilter(request, response);
	}

}
