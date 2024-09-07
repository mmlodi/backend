package com.meusboleto.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.meusboleto.backend.DTO.AccessDTO;
import com.meusboleto.backend.DTO.AuthenticationDTO;
import com.meusboleto.backend.security.jwt.JwtUtils;
@Service
public class AuthService {

	@Autowired
	private AuthenticationManager authenticatioManager;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	public AccessDTO login(AuthenticationDTO authDto) {
		try {
			// Cria mecanismo de credencial para o Spring
			UsernamePasswordAuthenticationToken userAuth = 
					new UsernamePasswordAuthenticationToken(authDto.getUsername(), authDto.getPassword());
	
			// Prepara mecanismo para autenticacao
			Authentication authentication = authenticatioManager.authenticate(userAuth);
	
			// Busca usuario logado
			UserDetailsImpl userAuthenticate = (UserDetailsImpl) authentication.getPrincipal();
	
			// Gera o token JWT
			String token = jwtUtils.generateTokenFromUserDetailsImplementation(userAuthenticate);
			
			AccessDTO accessDTO = new AccessDTO(
				userAuthenticate.getId()
				, userAuthenticate.getEmail()
				, userAuthenticate.getUsername()
				, token);
			// Retorna o token no AccessDTO
			return accessDTO;
	
		} catch (BadCredentialsException e) {
			// Logando o erro para depuração
			System.err.println("Login failed: Invalid username or password");
		} catch (AuthenticationException e) {
			// Para outros tipos de falhas de autenticação
			System.err.println("Authentication failed: " + e.getMessage());
		} catch (Exception e) {
			// Captura geral para qualquer outro tipo de exceção
			System.err.println("An error occurred during login: " + e.getMessage());
		}
	
		// Em caso de falha de autenticação, retorna acesso negado
		return new AccessDTO();
	}

}