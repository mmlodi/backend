package com.meusboleto.backend.rest;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.meusboleto.backend.DTO.AuthenticationDTO;
import com.meusboleto.backend.service.AuthService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;



@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;

    @Value("${projeto.jwtSecret}")
    private String jwtSecret;

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationDTO authenticationDTO) {
        //TODO: process POST request
        
        return ResponseEntity.ok(authService.login(authenticationDTO));
    }

    @GetMapping(value = "/verify-token")
    public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Check if the Authorization header is present and well-formed
            String token = authorizationHeader.substring(7); // Remove "Bearer " prefix
            boolean isTokenValid = validateJwtToken(token);
            
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ") && isTokenValid) {
                // Extract the token from the Authorization header
                
                // Parse and validate the JWT
                return ResponseEntity.ok().build(); // Token is valid
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Authorization header");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token validation failed: " + e.getMessage());
        }
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie deleteCookie = ResponseCookie.from("jwt", "")
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(0)
            .build();
        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());
        return ResponseEntity.ok().build();
    }

    public boolean validateJwtToken(String authToken){
        try {
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
            Jwts.parser().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            throw new MalformedJwtException("Token Inválido "+e.getMessage());
        } catch (ExpiredJwtException e) {
            //throw new ExpiredJwtException("Token Expirado "+e.getMessage());
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtException("Token Não suportado "+e.getMessage());
        } catch (Exception e) {
            //throw Exception("Token Ilegal " + e.getMessage());
        }

        return false;
    }
    
}
