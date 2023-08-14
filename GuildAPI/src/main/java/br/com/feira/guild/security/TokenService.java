package br.com.feira.guild.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.feira.guild.to.Customer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

	@Value("${fair.jwt.expiration}")
	private String expiration;

	@Value("${fair.jwt.secret}")
	private String secret;
	
	@Value("${fair.jwt.issuer}")
	private String issuer;

	public String generateToken(Authentication autentication) {
		Customer customer = (Customer) autentication.getPrincipal();

		Date now = new Date();

		return Jwts.builder().setIssuer(issuer).setSubject(customer.getId().toString())
				.setIssuedAt(now).setExpiration(new Date(now.getTime() + Long.parseLong(expiration)))
				.signWith(SignatureAlgorithm.HS256, secret).compact();
	}

	public boolean isValidToken(String token) {
		try {
			Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
			return true;
		} catch (Exception e) {		
			return false;
		}		
	}

	public Integer getIdUser(String token) {
		Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
		return Integer.parseInt(claims.getSubject());
	}
}
