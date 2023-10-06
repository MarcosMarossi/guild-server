package br.com.feira.guild.security;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.feira.guild.repository.CustomerRepository;
import br.com.feira.guild.to.Customer;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthenticationService implements UserDetailsService {
	
	private static final String AUTH_TOKEN_HEADER_NAME = "API_KEY";

	@Value("${fair.api.keys}")
	private String apiKeys;

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Customer> customer = customerRepository.findByEmail(username);
		if (customer.isPresent()) {
			return customer.get();
		}
		
		throw new UsernameNotFoundException("Invalid data!");
	}

	public Authentication getAuthentication(HttpServletRequest request) {
		List<String> keys = Arrays.asList(apiKeys.split(";"));
		String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);
		
		if (apiKey != null && keys.contains(apiKey)) {
			return new ApiKeyAuthentication(apiKey, AuthorityUtils.NO_AUTHORITIES);
		}
		
		throw new BadCredentialsException("Invalid API Key");
	}
}
