package br.com.feira.guild.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.feira.guild.repository.ProductRepository;
import br.com.feira.guild.to.Product;
import br.com.feira.guild.to.form.ProductForm;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;

	public List<Product> findAll() {
		List<Product> products = productRepository.findAll();
		products.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));
		
		return products;
	}

	public void save(ProductForm productForm) {
		productRepository.save(productForm.convertToProduct());
	}
	
}
