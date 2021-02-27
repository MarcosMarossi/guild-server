package br.com.suafeira.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.suafeira.repository.CustomerRepository;
import br.com.suafeira.repository.FairRepository;
import br.com.suafeira.service.CustomerService;
import br.com.suafeira.to.Customer;
import br.com.suafeira.to.Fair;
import br.com.suafeira.to.Product;
import br.com.suafeira.to.dto.CustomerDTO;
import br.com.suafeira.to.dto.handler.FairForm;
import br.com.suafeira.to.form.CustomerFairForm;
import br.com.suafeira.to.form.CustomerForm;
import br.com.suafeira.to.form.UpdateForm;

@RestController
@RequestMapping("/customers")
public class CustomerController {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private FairRepository fr;
	
	@Autowired
	private CustomerService service;
	
	@PostMapping
	@Description(value = "Register new users with a some products and fairs.")
	public ResponseEntity<?> register(@RequestBody CustomerForm form) {		
		try {	
			service.insert(form);	
			return new ResponseEntity<>(HttpStatus.CREATED);			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<CustomerDTO> findFairIdCustomer(@PathVariable(value = "id") Integer id) {		
		try {			
			Optional<Customer> client = customerRepository.findById(id);
			if(client.isPresent()) {
				Set<Product> products = client.get().getProducts();
				
				List<Product> convertedList = products.stream().collect(Collectors.toList());				
				convertedList.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));
				
				CustomerDTO customer = new CustomerDTO(client.get().getName(), client.get().getWhatsapp(), client.get().getEmail(), client.get().getFairs(), convertedList);
				
				return new ResponseEntity<CustomerDTO>(customer, HttpStatus.OK);				
			}
		
			throw new EntityNotFoundException("It not found with Id= " + id);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<CustomerDTO>(HttpStatus.BAD_REQUEST);
		}			
	}
	
	@PostMapping(value = "/newfair")
	public ResponseEntity<?> newFair(@RequestBody CustomerFairForm cfForm) {
		try {
			Optional<Customer> client = customerRepository.findById(cfForm.getCustomerId());
			Customer customer = client.get();
			
			Set<Fair> fairs = new TreeSet<Fair>();
			fairs = customer.getFairs();		
			
			for(FairForm handler : cfForm.getIdsFair()) {
				Optional<Fair> fair = fr.findById(handler.getIdFair());
				fairs.add(fair.get());
			}		
			customer.setFairs(fairs);		
			customerRepository.save(customer);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@DeleteMapping
	public ResponseEntity<?> delete(@RequestParam Integer customerId, @RequestParam Integer fairId) {
		try {
			Optional<Customer> client = customerRepository.findById(customerId);
			
			if(client.isPresent()) {
				Customer customer = client.get();
				
				Set<Fair> fairs = new TreeSet<Fair>();
				fairs = customer.getFairs();			
				
				Optional<Fair> fair = fr.findById(fairId);
				
				if(!fair.isPresent()) {
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
				
				fairs.remove(fair.get());
				
				customer.setFairs(fairs);		
				customerRepository.save(customer);			
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}	
	}
	
	@PatchMapping
	public ResponseEntity<?> update(@RequestBody UpdateForm form){
		try {
			Optional<Customer> client = customerRepository.findByEmail(form.getEmail());
			if(client.isPresent()) {
				Customer customer = client.get();
				
				if(isValidNumber(form)) {
					customer.setWhatsapp(form.getWhatsapp());
				}
				
				if(isValidPassword(form)) {
					String registerPassword = new BCryptPasswordEncoder().encode(form.getCustomerNewPassword());
					customer.setCustomerPassword(registerPassword);					
				}
				
				customerRepository.save(customer);
				return new ResponseEntity<>(HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}		
	}

	private boolean isValidNumber(UpdateForm form) {
		return !form.getWhatsapp().isEmpty() || form.getWhatsapp().length() >= 11;
	}

	private boolean isValidPassword(UpdateForm form) {
		return !form.getCustomerNewPassword().isEmpty() || form.getCustomerNewPassword().length() >= 8;
	}
}