package br.com.suafeira.to.form;

public class UpdateForm {
	
	private String whatsapp;
	private String email;
	private String customerNewPassword;
	
	public String getWhatsapp() {
		return whatsapp;
	}
	public void setWhatsapp(String whatsapp) {
		this.whatsapp = whatsapp;
	}
	public String getCustomerNewPassword() {
		return customerNewPassword;
	}
	public void setCustomerNewPassword(String customerNewPassword) {
		this.customerNewPassword = customerNewPassword;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}	
	
	public boolean isValidWhatsApp() {
		return !this.getWhatsapp().isEmpty() || this.getWhatsapp().length() >= 11;
	}

	public boolean isValidPassword() {
		return !this.getCustomerNewPassword().isEmpty() || this.getCustomerNewPassword().length() >= 8;
	}
}
