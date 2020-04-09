package me.chillgu.jwt.jwt;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {
	
	@Value("${spring.jwt.secret}")
	private String secretKey;
	
	private long validTokenTime = 1000L * 60 * 60;
	
	@PostConstruct
	public void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}
	
	
	public String createToken(String username, List<String> roles) {
		
		Date now = new Date();
		
		Claims claims = Jwts.claims()
							.setSubject(username)
							.setIssuedAt(now)
							.setExpiration(new Date(now.getTime() + validTokenTime));
		
		claims.put("roles", roles);
		
		String jwt = Jwts.builder()
							.setHeaderParam("typ", "JWT")
							.setClaims(claims)
							.signWith(SignatureAlgorithm.HS256, secretKey)
							.compact();
		
		return jwt;
	}
	
	public boolean validateToken(String token) {
		
		try {
			Claims claims = Jwts.parser()
								.setSigningKey(secretKey)
								.parseClaimsJws(token)
								.getBody();
			
			return !claims.getExpiration().before(new Date());
		} catch(Exception e) {
			
			return false;
		}
	}
	
	public String getUsername(String token) {
		
		return Jwts.parser()
					.setSigningKey(secretKey)
					.parseClaimsJws(token)
					.getBody()
					.getSubject();
	}
	
}
