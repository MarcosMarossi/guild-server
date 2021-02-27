package br.com.suafeira.handler;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import br.com.suafeira.repository.FairRepository;
import br.com.suafeira.repository.ProductRepository;
import br.com.suafeira.to.Fair;
import br.com.suafeira.to.Product;
import br.com.suafeira.to.dto.handler.FairForm;
import br.com.suafeira.to.dto.handler.ProductForm;
import br.com.suafeira.to.form.CustomerForm;

public class CustomerHandler {
	
	public static Set<Fair> getFairs(CustomerForm form, FairRepository repository) {
		Set<Fair> fairs = new HashSet<Fair>();			
		List<FairForm> idsFair = form.getIdsFair();	
		
		if(idsFair == null || idsFair.isEmpty())
			throw new NullPointerException("The fields can't be nullable.");
		
		
		idsFair.forEach(model -> {
			Optional<Fair> fair = repository.findById(model.getIdFair());
			fairs.add(fair.get());	
		});
		return fairs;
	}
	
	public static Set<Product> getProducts(CustomerForm form, ProductRepository repository) {
		Set<Product> products = new HashSet<Product>();			
		List<ProductForm> idsProduct = form.getIdsProduct();
		
		if(idsProduct == null || idsProduct.isEmpty())
		throw new NullPointerException("The fields can't be nullable.");
		
		idsProduct.forEach(model -> {
			Optional<Product> product = repository.findById(model.getIdProduct());
			products.add(product.get());				
		});
		return products;
	}

}
