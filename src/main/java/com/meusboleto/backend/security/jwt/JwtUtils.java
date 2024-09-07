package com.meusboleto.backend.security.jwt;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.meusboleto.backend.service.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

    @Value("${projeto.jwtSecret}")
    private String jwtSecret;

    @Value("${projeto.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateTokenFromUserDetailsImplementation(UserDetailsImpl userData){
        return Jwts.builder()
            .setSubject(userData.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(new Date( new Date().getTime() + jwtExpirationMs))
            .signWith(getSignKey(),SignatureAlgorithm.HS512).compact();
    }

    
    public Key getSignKey() {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        return  key;
    }

    public String getUsernameToken(String token) {
		return Jwts.parser().setSigningKey(getSignKey()).build()
				.parseClaimsJws(token).getBody().getSubject();
	}

    public boolean validateJwtToken(String authToken){
        try {
            Jwts.parser().setSigningKey(getSignKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            System.out.println("Token Inválido "+e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("Token Expirado "+e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("Token Não suportado "+e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Token Ilegal "+e.getMessage());
        }

        return false;
    }
}
