package br.com.suafeira.to.form;

import java.util.List;

import br.com.suafeira.to.Customer;
import br.com.suafeira.to.dto.handler.FairHandler;
import br.com.suafeira.to.dto.handler.ProductHandler;

public class CustomerForm {
	
	private String name;
	private String email;	
	private String whatsapp;
	private String customerPassword;	
	private List<ProductHandler> idsProduct;
	private List<FairHandler> idsFair;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWhatsapp() {
		return whatsapp;
	}
	public void setWhatsapp(String whatsapp) {
		this.whatsapp = whatsapp;
	}	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCustomerPassword() {
		return customerPassword;
	}
	public void setCustomerPassword(String customerPassword) {
		this.customerPassword = customerPassword;
	}		
	public List<ProductHandler> getIdsProduct() {
		return idsProduct;
	}
	public void setIdsProduct(List<ProductHandler> idsProduct) {
		this.idsProduct = idsProduct;
	}
	public List<FairHandler> getIdsFair() {
		return idsFair;
	}
	public void setIdsFair(List<FairHandler> idsFair) {
		this.idsFair = idsFair;
	}
	public Customer convertToCustomer() {
		return new Customer(name, whatsapp, email, customerPassword);
	}
}
