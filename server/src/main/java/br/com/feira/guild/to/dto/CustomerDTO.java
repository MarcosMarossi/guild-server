package br.com.feira.guild.to.dto;

import java.util.List;
import java.util.Set;

import br.com.feira.guild.to.Fair;
import br.com.feira.guild.to.Product;

public class CustomerDTO {
	
	private String name;
	private String whatsapp;
	private String email;
	private Set<Fair> fairs;
	private List<Product> products;	
	
	public CustomerDTO(String name, String whatsapp, String email, Set<Fair> fairs, List<Product> products) {
		this.name = name;
		this.whatsapp = whatsapp;
		this.email = email;
		this.fairs = fairs;
		this.products = products;
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
	public Set<Fair> getFairs() {
		return fairs;
	}
	public void setFairs(Set<Fair> fairs) {
		this.fairs = fairs;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	public List<Product> getProducts() {
		return products;
	}
}
