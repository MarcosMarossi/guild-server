package br.com.feira.guild.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.feira.guild.repository.CustomerRepository;
import br.com.feira.guild.service.TokenService;
import br.com.feira.guild.to.Customer;

public class AuthenticationTokenFilter extends OncePerRequestFilter {
	
	private TokenService tokenService;
	
	private CustomerRepository usuarioRepository;
	
	public AuthenticationTokenFilter(TokenService tokenService, CustomerRepository usuarioRepository) {
		this.tokenService = tokenService;
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = recuerarToken(request);
		
		boolean valido = tokenService.isValidToken(token);
		if(valido) {
			autenticarCliente(token);
		}
		
		filterChain.doFilter(request, response);		
	}

	private void autenticarCliente(String token) {
		Integer idUsuario = tokenService.getIdUser(token);
		Customer usuario = usuarioRepository.findById(idUsuario).get();
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
	}

	private String recuerarToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if(token == null || token.isEmpty() || !token.startsWith("Bearer " )) {
			return null;
		}
		return token.substring(7, token.length());
	}

}
