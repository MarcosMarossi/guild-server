package br.com.suafeira.to.form;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class LoginForm {

	private String email;
	private String customerPassword;

	public String getEmail() {
		return email;
	}

	public String getCustomerPassword() {
		return customerPassword;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setCustomerPassword(String customerPassword) {
		this.customerPassword = customerPassword;
	}

	public UsernamePasswordAuthenticationToken converter() {
		return new UsernamePasswordAuthenticationToken(email, customerPassword);
	}

}
