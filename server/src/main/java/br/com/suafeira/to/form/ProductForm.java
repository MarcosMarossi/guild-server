package br.com.suafeira.to.form;

import br.com.suafeira.to.ProductTO;

public class ProductForm {
	
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public ProductTO convertToProduct() {
		return new ProductTO(name);
	}
}
