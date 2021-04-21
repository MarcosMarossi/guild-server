package br.com.suafeira.to.form;

import br.com.suafeira.to.ProductTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ProductForm {
	
	private String name;
	private Integer idProduct;
	
	public ProductTO convertToProduct() {
		return new ProductTO(name);
	}
	
}
