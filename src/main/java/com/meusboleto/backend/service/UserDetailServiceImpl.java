package com.meusboleto.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.meusboleto.backend.model.User;
import com.meusboleto.backend.repository.UserRepository;

@Service
public class UserDetailServiceImpl implements  UserDetailsService {

	@Autowired
	private UserRepository usuarioRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User usuario = usuarioRepository.findByUserName(username).get();
		return UserDetailsImpl.build(usuario);
	}

}
