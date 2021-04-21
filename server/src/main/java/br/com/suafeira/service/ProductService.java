package br.com.suafeira.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.suafeira.repository.ProductRepository;
import br.com.suafeira.to.ProductTO;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;

	public ProductTO save(ProductTO product) {
		return productRepository.save(product);		
	}

	public List<ProductTO> findAll() {
		return productRepository.findAll();
	}

}
