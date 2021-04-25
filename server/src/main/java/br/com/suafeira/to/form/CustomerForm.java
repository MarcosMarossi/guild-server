package br.com.suafeira.to.form;

import java.util.List;

import br.com.suafeira.to.CustomerTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class CustomerForm {
	
	private String name;
	private String email;	
	private String whatsapp;
	private String customerPassword;
	private List<FairForm> idsFair;
	private List<ProductForm> idsProduct;
	
	public CustomerTO convertToCustomer() {
		return new CustomerTO(name, whatsapp, email, customerPassword);
	}
}
