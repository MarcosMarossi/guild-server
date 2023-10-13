package br.com.feira.guild.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.feira.guild.controller.form.ProductForm;
import br.com.feira.guild.service.ProductService;
import br.com.feira.guild.to.Product;

@RestController
@RequestMapping("/products")
public class ProductController {
		
	@Autowired
	private ProductService productService;
	
	private static final Logger logger = LogManager.getLogger(ProductController.class);
	
	@PostMapping
	public ResponseEntity<?> register(@RequestBody ProductForm productForm) {
		
		logger.info("Entering registration.");
		long initialTime = System.currentTimeMillis();
		
		try {
			Product product = productService.save(productForm);
			return new ResponseEntity<>(product, HttpStatus.CREATED); 
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {
			long finalTime = System.currentTimeMillis();
			logger.info("Exiting registration in " + (finalTime - initialTime) + " s.");
		}
		
	}
	
	@GetMapping
	public ResponseEntity<?> findAll() {		
		logger.info("Entering find all products.");
		long initialTime = System.currentTimeMillis();
		
		try {
			List<Product> products = productService.findAll();
			return new ResponseEntity<>(products, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {
			long finalTime = System.currentTimeMillis();
			logger.info("Exiting find all products in " + (finalTime - initialTime) + " s.");
		}
		
	}
	
	@PutMapping
	public ResponseEntity<?> update(@RequestBody ProductForm form) {		
		logger.info("Entering update product.");
		long initialTime = System.currentTimeMillis();
		
		try {
			productService.update(form);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {
			long finalTime = System.currentTimeMillis();
			logger.info("Exiting update product in " + (finalTime - initialTime) + " s.");
		}		
	}
	
	@DeleteMapping
	public ResponseEntity<?> delete(@RequestParam Integer id) {		
		logger.info("Entering delete product.");
		long initialTime = System.currentTimeMillis();
		
		try {
			productService.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {
			long finalTime = System.currentTimeMillis();
			logger.info("Exiting delete product in " + (finalTime - initialTime) + " s.");
		}		
	}
}
