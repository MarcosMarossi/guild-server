package br.com.suafeira.to.dto.handler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.suafeira.to.Customer;
import br.com.suafeira.to.Product;

public class CustomerHandler {
	
	private String name;
	private String whatsapp;
	private String email;
	private String listProduct;
	
	public CustomerHandler() {
	}
	
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
	public String getListProduct() {
		return listProduct;
	}
	public void setListProduct(String listProduct) {
		this.listProduct = listProduct;
	}
	
	public Set<CustomerHandler> convert(Set<Customer> customers) {
		Set<CustomerHandler> convertObject = new HashSet<CustomerHandler>();
		
		for(Customer customer : customers) {
			CustomerHandler handler = new CustomerHandler();
			handler.setEmail(customer.getEmail());
			handler.setName(customer.getName());
			handler.setWhatsapp(customer.getWhatsapp());
			handler.setListProduct(extractForString(customer));
			convertObject.add(handler);
		}		
		
		return convertObject;		
	}
	
	private String extractForString(Customer customer) {
		StringBuilder str = new StringBuilder();
		
		Set<Product> products = customer.getProducts();	
		
		List<Product> convertedList = products.stream().collect(Collectors.toList());
		convertedList.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));
		
		convertedList.forEach(product -> {
			str.append(product.getName() + ", ");
		});
		return str.toString().substring (0, str.length() - 2) + ".";
	}
}
