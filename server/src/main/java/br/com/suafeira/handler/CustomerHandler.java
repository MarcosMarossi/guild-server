package br.com.suafeira.handler;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import br.com.suafeira.repository.FairRepository;
import br.com.suafeira.repository.ProductRepository;
import br.com.suafeira.to.FairTO;
import br.com.suafeira.to.ProductTO;
import br.com.suafeira.to.form.CustomerForm;
import br.com.suafeira.to.form.FairForm;
import br.com.suafeira.to.form.ProductForm;

public class CustomerHandler {
	
	public static Set<FairTO> getFairs(CustomerForm form, FairRepository repository) {
		Set<FairTO> fairs = new HashSet<FairTO>();			
		List<FairForm> idsFair = form.getIdsFair();	
		
		if(idsFair == null || idsFair.isEmpty())
			throw new NullPointerException("The fields can't be nullable.");
		
		
		idsFair.forEach(model -> {
			Optional<FairTO> fair = repository.findById(model.getIdFair());
			fairs.add(fair.get());	
		});
		return fairs;
	}
	
	public static Set<ProductTO> getProducts(CustomerForm form, ProductRepository repository) {
		Set<ProductTO> products = new HashSet<ProductTO>();			
		List<ProductForm> idsProduct = form.getIdsProduct();
		
		if(idsProduct == null || idsProduct.isEmpty())
		throw new NullPointerException("The fields can't be nullable.");
		
		idsProduct.forEach(model -> {
			Optional<ProductTO> product = repository.findById(model.getIdProduct());
			products.add(product.get());				
		});
		return products;
	}

}
