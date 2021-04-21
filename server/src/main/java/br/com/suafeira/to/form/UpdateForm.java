package br.com.suafeira.to.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class UpdateForm {
	
	private String whatsapp;
	private String email;
	private String customerNewPassword;	
	
	public boolean isValidWhatsApp() {
		return !this.getWhatsapp().isEmpty() || this.getWhatsapp().length() >= 11;
	}

	public boolean isValidPassword() {
		return !this.getCustomerNewPassword().isEmpty() || this.getCustomerNewPassword().length() >= 8;
	}
}
