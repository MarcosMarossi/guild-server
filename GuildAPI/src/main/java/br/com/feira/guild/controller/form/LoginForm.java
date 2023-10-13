package br.com.feira.guild.controller.form;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginForm {

	private String email;
	private String customerPassword;

	public UsernamePasswordAuthenticationToken converter() {
		return new UsernamePasswordAuthenticationToken(email, customerPassword);
	}

}
