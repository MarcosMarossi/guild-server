package br.com.feira.guild.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.feira.guild.repository.CustomerRepository;
import br.com.feira.guild.to.Customer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {
	
	private TokenService tokenService;
	
	private CustomerRepository customerRepository;
	
	public AuthenticationTokenFilter(TokenService tokenService, CustomerRepository customerRepository) {
		this.tokenService = tokenService;
		this.customerRepository = customerRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = getToken(request);
		
		if(tokenService.isValidToken(token)) {
			Integer idCustomer = tokenService.getIdUser(token);
			Customer customer = customerRepository.findById(idCustomer).get();
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(customer, null, customer.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);	
		}
		
		filterChain.doFilter(request, response);		
	}

	private String getToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if(token == null || token.isEmpty() || !token.startsWith("Bearer " )) {
			return null;
		}
		return token.substring(7, token.length());
	}

}
