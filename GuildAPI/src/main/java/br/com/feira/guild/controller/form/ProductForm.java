package br.com.feira.guild.controller.form;

import br.com.feira.guild.to.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ProductForm {
	
	private Integer id;
	private String name;
	
	public Product convertToProduct() {
		return new Product(name);
	}

	public ProductForm(String name) {
		super();
		this.name = name;
	}
	
}
