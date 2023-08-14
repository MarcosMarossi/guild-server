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

	public String gerarToken(Authentication autentication) {
		Customer usuario = (Customer) autentication.getPrincipal();

		Date hoje = new Date();

		return Jwts.builder().setIssuer("Api do Fórum da Alura").setSubject(usuario.getId().toString())
				.setIssuedAt(hoje).setExpiration(new Date(hoje.getTime() + Long.parseLong(expiration)))
				.signWith(SignatureAlgorithm.HS256, secret).compact();
	}

	public boolean isTokenValido(String token) {
		try {
			Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
			return true;
		} catch (Exception e) {		
			return false;
		}		
	}

	public Integer getIdUsuario(String token) {
		Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
		return Integer.parseInt(claims.getSubject());
	}
}
