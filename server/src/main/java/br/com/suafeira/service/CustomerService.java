package br.com.suafeira.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.suafeira.handler.CustomerHandler;
import br.com.suafeira.repository.CustomerRepository;
import br.com.suafeira.repository.FairRepository;
import br.com.suafeira.repository.ProductRepository;
import br.com.suafeira.to.CustomerTO;
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
		String encryptPassword = new BCryptPasswordEncoder().encode(form.getCustomerPassword());		
		form.setCustomerPassword(encryptPassword);
		
		CustomerTO customer = form.convertToCustomer();
		
		customer.setProducts(CustomerHandler.getProducts(form, productRepository));
		customer.setFairs(CustomerHandler.getFairs(form, fairRepository));
		
		customerRepository.save(customer);
	}
}
