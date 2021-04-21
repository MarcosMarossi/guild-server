package br.com.suafeira.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.suafeira.handler.CustomerHandler;
import br.com.suafeira.repository.CustomerRepository;
import br.com.suafeira.repository.FairRepository;
import br.com.suafeira.repository.ProductRepository;
import br.com.suafeira.to.CustomerTO;
import br.com.suafeira.to.ProductTO;
import br.com.suafeira.to.dto.CustomerDTO;
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
	
	public CustomerDTO getCustomer(Optional<CustomerTO> client) {
		Set<ProductTO> products = client.get().getProducts();
		
		List<ProductTO> convertedList = products.stream().collect(Collectors.toList());				
		convertedList.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));
		
		return new CustomerDTO(client.get().getName(), client.get().getWhatsapp(), 
				client.get().getEmail(), client.get().getFairs(), convertedList
		);
	}
	
	public Optional<CustomerTO> findById(Integer id) {
		return customerRepository.findById(id);
	}

	public Optional<CustomerTO> findByEmail(String email) {
		return customerRepository.findByEmail(email);
	}
}
