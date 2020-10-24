package br.com.suafeira.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
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
import br.com.suafeira.repository.ProductRepository;
import br.com.suafeira.to.Customer;
import br.com.suafeira.to.Fair;
import br.com.suafeira.to.Product;
import br.com.suafeira.to.dto.CustomerDTO;
import br.com.suafeira.to.dto.handler.FairHandler;
import br.com.suafeira.to.dto.handler.ProductHandler;
import br.com.suafeira.to.form.CustomerFairForm;
import br.com.suafeira.to.form.CustomerForm;
import br.com.suafeira.to.form.UpdateForm;

@RestController
@RequestMapping("/customers")
public class CustomerController {
	
	@Autowired
	private CustomerRepository cr;
	
	@Autowired
	private FairRepository fr;
	
	@Autowired
	private ProductRepository pr;
	
	@PostMapping
	public ResponseEntity<?> register(@RequestBody CustomerForm register) {		
		try {			
			String registerPassword = new BCryptPasswordEncoder().encode(register.getCustomerPassword());			
			
			if(registerPassword.isEmpty() || registerPassword.isBlank()) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}		
			register.setCustomerPassword(registerPassword);
			
			Set<Fair> fairs = new HashSet<Fair>();			
			List<FairHandler> idsFair = register.getIdsFair();			
			
			idsFair.forEach(model -> {
				Optional<Fair> fair = fr.findById(model.getIdFair());
				fairs.add(fair.get());	
			});
			
			if(idsFair == null || idsFair.isEmpty())
				throw new NullPointerException("The fields can't be nullable.");
			
			Set<Product> products = new HashSet<Product>();			
			List<ProductHandler> idsProduct = register.getIdsProduct();
			
			idsProduct.forEach(model -> {
				Optional<Product> product = pr.findById(model.getIdProduct());
				products.add(product.get());				
			});
			
			if(idsProduct == null || idsProduct.isEmpty())
				throw new NullPointerException("The fields can't be nullable.");
			
			Customer customer = register.convertToCustomer();
			customer.setProducts(products);
			customer.setFairs(fairs);
			
			cr.save(customer);			
			return new ResponseEntity<>(HttpStatus.CREATED);			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping
	public ResponseEntity<?> findAll() {
		return new ResponseEntity<>(cr.findAll(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<CustomerDTO> findFairIdCustomer(@PathVariable(value = "id") Integer id) {		
		try {			
			Optional<Customer> client = cr.findById(id);
			if(client.isPresent()) {
				CustomerDTO customer = new CustomerDTO(client.get().getName(), client.get().getWhatsapp(), client.get().getEmail(), client.get().getFairs(), client.get().getProducts());
				return new ResponseEntity<CustomerDTO>(customer, HttpStatus.OK);				
			}
		
			throw new EntityNotFoundException("It not found with Id= " + id);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<CustomerDTO>(HttpStatus.BAD_REQUEST);
		}			
	}
	
	@PostMapping(value = "/newfair")
	public ResponseEntity<?> update(@RequestBody CustomerFairForm cfForm) {
		try {
			Optional<Customer> client = cr.findById(cfForm.getCustomerId());
			Customer customer = client.get();
			
			Set<Fair> fairs = new TreeSet<Fair>();
			fairs = customer.getFairs();		
			
			for(FairHandler handler : cfForm.getIdsFair()) {
				Optional<Fair> fair = fr.findById(handler.getIdFair());
				fairs.add(fair.get());
			}		
			customer.setFairs(fairs);		
			cr.save(customer);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@DeleteMapping(value = "/delete")
	public ResponseEntity<?> delete(@RequestParam Integer customerId, @RequestParam Integer fairId) {
		try {
			Optional<Customer> client = cr.findById(customerId);
			Customer customer = client.get();
			
			Set<Fair> fairs = new TreeSet<Fair>();
			fairs = customer.getFairs();			
			
			Optional<Fair> fair = fr.findById(fairId);
			fairs.remove(fair.get());
			
			customer.setFairs(fairs);		
			cr.save(customer);			
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}	
	}
	
	@PatchMapping
	public ResponseEntity<?> update(@RequestBody UpdateForm form){
		try {
			Optional<Customer> client = cr.findByEmail(form.getEmail());
			if(client.isPresent()) {
				Customer customer = client.get();
				
				if(isValidNumber(form)) {
					customer.setWhatsapp(form.getWhatsapp());
				}
				
				if(isValidPassword(form)) {
					String registerPassword = new BCryptPasswordEncoder().encode(form.getCustomerNewPassword());
					customer.setCustomerPassword(registerPassword);					
				}
				
				cr.save(customer);
				return new ResponseEntity<>(HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}		
	}

	private boolean isValidNumber(UpdateForm form) {
		return !form.getWhatsapp().isBlank() || form.getWhatsapp().length() >= 11;
	}

	private boolean isValidPassword(UpdateForm form) {
		return !form.getCustomerNewPassword().isBlank() || form.getCustomerNewPassword().length() >= 8;
	}
}