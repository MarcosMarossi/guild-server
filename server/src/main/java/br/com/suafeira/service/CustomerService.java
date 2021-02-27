package br.com.suafeira.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.suafeira.handler.CustomerHandler;
import br.com.suafeira.repository.CustomerRepository;
import br.com.suafeira.repository.FairRepository;
import br.com.suafeira.repository.ProductRepository;
import br.com.suafeira.to.Customer;
import br.com.suafeira.to.form.CustomerForm;

@Service
public class CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private FairRepository fairRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	public void insert(CustomerForm form) {		
		if(form.getCustomerPassword().isEmpty()) 
			throw new RuntimeException("");
		
		form.setCustomerPassword(encryptedCaracter(form));
		Customer customer = form.convertToCustomer();
		customer.setProducts(CustomerHandler.getProducts(form, productRepository));
		customer.setFairs(CustomerHandler.getFairs(form, fairRepository));
		
		customerRepository.save(customer);
	}
	
	private String encryptedCaracter(CustomerForm form) {		
		return new BCryptPasswordEncoder().encode(form.getCustomerPassword());
	}

}
