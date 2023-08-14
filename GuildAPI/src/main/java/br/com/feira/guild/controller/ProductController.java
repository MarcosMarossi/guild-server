package br.com.feira.guild.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.feira.guild.repository.ProductRepository;
import br.com.feira.guild.to.Product;
import br.com.feira.guild.to.form.ProductForm;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	@Autowired
	private ProductRepository productRepository;
	
	@PostMapping
	@CacheEvict(value = "findProducts", allEntries = true)
	public ResponseEntity<?> register(@RequestBody ProductForm productForm) {
		productRepository.save(productForm.convertToProduct());
		return new ResponseEntity<>(HttpStatus.CREATED); 		
	}
	
	@GetMapping
	@Cacheable(value = "findProducts")
	public ResponseEntity<?> findAll() {
		List<Product> products = productRepository.findAll();
		products.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));
		return new ResponseEntity<>(products, HttpStatus.OK);
	}
}
