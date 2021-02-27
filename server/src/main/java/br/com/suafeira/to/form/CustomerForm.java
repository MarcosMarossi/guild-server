package br.com.suafeira.to.form;

import java.util.List;

import br.com.suafeira.to.CustomerTO;
import br.com.suafeira.to.dto.handler.FairForm;
import br.com.suafeira.to.dto.handler.ProductForm;

public class CustomerForm {
	
	private String name;
	private String email;	
	private String whatsapp;
	private String customerPassword;	
	private List<ProductForm> idsProduct;
	private List<FairForm> idsFair;
	
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
	public List<ProductForm> getIdsProduct() {
		return idsProduct;
	}
	public void setIdsProduct(List<ProductForm> idsProduct) {
		this.idsProduct = idsProduct;
	}
	public List<FairForm> getIdsFair() {
		return idsFair;
	}
	public void setIdsFair(List<FairForm> idsFair) {
		this.idsFair = idsFair;
	}
	public CustomerTO convertToCustomer() {
		return new CustomerTO(name, whatsapp, email, customerPassword);
	}
}
