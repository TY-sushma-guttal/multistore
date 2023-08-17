package com.mrmrscart.productcategoriesservice.utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrmrscart.productcategoriesservice.response.category.ExceptionResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Value("${jwt.secret}")
	private String secret;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String header = request.getHeader("Authorization");
		if (header != null && header.startsWith("Bearer ")) {
			try {
				String token = header.substring("Bearer ".length());
				Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
				JWTVerifier verifier = JWT.require(algorithm).build();
				DecodedJWT decodedJWT = verifier.verify(token);
				String username = decodedJWT.getSubject();
				List<String> asList = decodedJWT.getClaim("roles").asList(String.class);
				List<SimpleGrantedAuthority> authorities = new ArrayList<>();
				asList.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_"+role)));
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						username, null, authorities);
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				filterChain.doFilter(request, response);
			} catch (Exception e) {
				ExceptionResponse exceptionResponse=new ExceptionResponse();
				exceptionResponse.setError(true);
				exceptionResponse.setMessage("Invalid Token. ");
				ObjectMapper mapper = new ObjectMapper();
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				response.getWriter().write(mapper.writeValueAsString(exceptionResponse));
				
			}

		} else {
			filterChain.doFilter(request, response);
		}
	}

}
