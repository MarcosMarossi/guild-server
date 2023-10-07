package br.com.feedback.security;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthenticationService implements UserDetailsService {

	private static final String AUTH_TOKEN_HEADER_NAME = "FEEDBACK_API_KEY";
	
	@Value("${fair.api.keys}")
	private String apiKeys;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return null;
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
