package br.com.suafeira.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.suafeira.repository.CustomerRepository;
import br.com.suafeira.service.TokenService;
import br.com.suafeira.to.CustomerTO;
import br.com.suafeira.to.LoginTO;
import br.com.suafeira.to.dto.TokenDTO;

@RestController 
@RequestMapping (value = "/auth")
public class AuthenticationController {
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@PostMapping
	public ResponseEntity<?> autenticar( @RequestBody @Valid LoginTO form) {
		Optional<CustomerTO> customer = customerRepository.findByEmail(form.getEmail());		
		
		if(!customer.isPresent()) 
			return ResponseEntity.notFound().build();
		
		UsernamePasswordAuthenticationToken dadosLogin = form.converter();
		
		try {
			Authentication autentication = authManager.authenticate(dadosLogin);
			String token = tokenService.gerarToken(autentication);
			return ResponseEntity.ok(new TokenDTO(token, "Bearer", customer.get().getId()));
			
		} catch(org.springframework.security.core.AuthenticationException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}
}
