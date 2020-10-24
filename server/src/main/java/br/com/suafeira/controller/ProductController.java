package br.com.suafeira.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.suafeira.repository.ProductRepository;
import br.com.suafeira.to.form.ProductForm;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	@Autowired
	private ProductRepository productRepository;
	
	@PostMapping
	public ResponseEntity<?> register(@RequestBody ProductForm productForm) {
		productRepository.save(productForm.convertToProduct());
		return new ResponseEntity<>(HttpStatus.CREATED); 		
	}
	
	@GetMapping
	public ResponseEntity<?> findAll() {
		return new ResponseEntity<>(productRepository.findAll(), HttpStatus.OK);
	}
}
