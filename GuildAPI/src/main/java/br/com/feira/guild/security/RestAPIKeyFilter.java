package br.com.feira.guild.security;

import java.io.IOException;
import java.io.PrintWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.feira.guild.controller.CustomerController;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RestAPIKeyFilter extends OncePerRequestFilter {
	
	private static final Logger logger = LogManager.getLogger(CustomerController.class);
	private AuthenticationService authenticationService;
	
    public RestAPIKeyFilter(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String servletPath = request.getServletPath();
			
			// It's not swagger documentation
			if(!servletPath.contains("/swagger-ui/") && !servletPath.contains("/v3/api-docs")) {
				Authentication authentication = authenticationService.getAuthentication((HttpServletRequest) request);
	            SecurityContextHolder.getContext().setAuthentication(authentication);
			}
        } catch (Exception exp) {
        	logger.error(exp.getMessage());
            
        	HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            
            PrintWriter writer = httpResponse.getWriter();
            writer.print(exp.getMessage());
            writer.flush();
            writer.close();
        }
        
        filterChain.doFilter(request, response);		
	}
    
}