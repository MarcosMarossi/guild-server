package br.com.feira.guild.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

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

import br.com.feira.guild.repository.CustomerRepository;
import br.com.feira.guild.repository.FairRepository;
import br.com.feira.guild.repository.ProductRepository;
import br.com.feira.guild.to.Customer;
import br.com.feira.guild.to.Fair;
import br.com.feira.guild.to.Product;
import br.com.feira.guild.to.dto.CustomerDTO;
import br.com.feira.guild.to.dto.handler.FairHandler;
import br.com.feira.guild.to.dto.handler.ProductHandler;
import br.com.feira.guild.to.form.CustomerFairForm;
import br.com.feira.guild.to.form.CustomerForm;
import br.com.feira.guild.to.form.UpdateForm;

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
			
			if(registerPassword.isEmpty()) {
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
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<CustomerDTO> findFairIdCustomer(@PathVariable(value = "id") Integer id) {		
		try {			
			Optional<Customer> client = cr.findById(id);
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
	
	@DeleteMapping
	public ResponseEntity<?> delete(@RequestParam Integer customerId, @RequestParam Integer fairId) {
		try {
			Optional<Customer> client = cr.findById(customerId);
			
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
				cr.save(customer);			
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
		return !form.getWhatsapp().isEmpty() || form.getWhatsapp().length() >= 11;
	}

	private boolean isValidPassword(UpdateForm form) {
		return !form.getCustomerNewPassword().isEmpty() || form.getCustomerNewPassword().length() >= 8;
	}
}