package br.com.feira.guild.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.feira.guild.exceptions.EntityNotFoundException;
import br.com.feira.guild.security.TokenService;
import br.com.feira.guild.service.CustomerService;
import br.com.feira.guild.to.Customer;
import br.com.feira.guild.to.dto.TokenDTO;
import br.com.feira.guild.to.form.LoginForm;

@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private CustomerService customerService;

	private static final Logger logger = LogManager.getLogger(AuthenticationController.class);

	@PostMapping
	public ResponseEntity<?> authenticate(@RequestBody LoginForm form) {
		logger.info("Entering authentication.");
		long initialTime = System.currentTimeMillis();

		try {
			UsernamePasswordAuthenticationToken loginData = form.converter();

			Authentication autentication = authManager.authenticate(loginData);
			String token = tokenService.generateToken(autentication);

			Customer customer = customerService.findByEmail(form.getEmail());

			return ResponseEntity.ok(new TokenDTO(token, "Bearer", customer.getId()));
		} catch (EntityNotFoundException e) {
			logger.error(e.getMessage());
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ResponseEntity.badRequest().build();
		} finally {
			long finalTime = System.currentTimeMillis();
			logger.info("Exiting authentication in " + (finalTime - initialTime) + " s.");
		}
	}
}
