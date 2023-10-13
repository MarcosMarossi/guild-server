package br.com.feira.guild.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.feira.guild.controller.form.ProductForm;
import br.com.feira.guild.exceptions.EntityNotFoundException;
import br.com.feira.guild.repository.ProductRepository;
import br.com.feira.guild.to.Product;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;

	public List<Product> findAll() {
		List<Product> products = productRepository.findAll();
		products.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));
		
		return products;
	}

	public Product save(ProductForm productForm) {
		return productRepository.save(productForm.convertToProduct());
	}

	public void update(ProductForm form) {
		Optional<Product> optProduct = productRepository.findById(form.getId());
		
		if(optProduct.isPresent()) {
			Product product = optProduct.get();
			product.setName(form.getName());
			productRepository.save(product);			
		} else {
			throw new EntityNotFoundException("Product with id " + form.getId() + " does not exists.");
		}
	}

	public void deleteById(Integer id) {
		Optional<Product> optProduct = productRepository.findById(id);
		
		if(optProduct.isPresent()) {
			productRepository.deleteById(id);			
		} else {
			throw new EntityNotFoundException("Product with id " + id + " does not exists.");
		}
	}
	
}
