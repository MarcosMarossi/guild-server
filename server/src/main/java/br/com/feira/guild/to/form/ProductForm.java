package br.com.feira.guild.to.form;

import br.com.feira.guild.to.Product;

public class ProductForm {
	
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Product convertToProduct() {
		return new Product(name);
	}
}
