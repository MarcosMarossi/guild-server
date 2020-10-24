package br.com.suafeira.to.form;

import br.com.suafeira.to.Product;

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
