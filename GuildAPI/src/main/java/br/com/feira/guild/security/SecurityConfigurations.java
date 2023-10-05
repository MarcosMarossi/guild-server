package br.com.feira.guild.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.feira.guild.repository.CustomerRepository;

@Configuration(enforceUniqueMethods = false)
@EnableWebSecurity
public class SecurityConfigurations {
	
	@Autowired
	private AuthenticationService autenticacaoService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private CustomerRepository usuarioRepository;
	
	@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

	@Bean
	AuthenticationManager authenticationManager(AuthenticationManagerBuilder builder, PasswordEncoder encoder)
			throws Exception {
		return builder.userDetailsService(autenticacaoService).passwordEncoder(encoder).and().build();
	}
	
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/customers").permitAll()
                .requestMatchers(HttpMethod.GET, "/customers/*").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth").permitAll()
                .requestMatchers(HttpMethod.GET, "/fairs").permitAll()
                .requestMatchers(HttpMethod.POST, "/fairs").permitAll()
                .requestMatchers(HttpMethod.GET, "/fairs/*").permitAll()
                .requestMatchers(HttpMethod.GET, "/fairs/search/*").permitAll()
                .requestMatchers(HttpMethod.POST, "/products").permitAll()
                .requestMatchers(HttpMethod.GET, "/products").permitAll()
                .anyRequest().authenticated()
                .and().csrf(csrf -> csrf.disable())
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).addFilterBefore(new AuthenticationTokenFilter(tokenService, usuarioRepository), UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
    }
	
	@Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/swagger-ui/**");
    }
}