package br.com.suafeira.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

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
import br.com.suafeira.to.CustomerTO;
import br.com.suafeira.to.FairTO;
import br.com.suafeira.to.dto.FairDTO;
import br.com.suafeira.to.dto.handler.CustomerForm;
import br.com.suafeira.to.form.CustomerFairForm;
import br.com.suafeira.to.form.FairForm;
import br.com.suafeira.to.form.UpdateForm;

@RestController
@RequestMapping("/fairs")
public class FairController {
	
	@Autowired
	private FairRepository fairRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@PostMapping
	public ResponseEntity<?> register(@RequestBody FairForm fair) {
		FairTO fairReturn = fairRepository.save(fair.convertToFair());
		return new ResponseEntity<>(fairReturn.getId() , HttpStatus.CREATED); 		
	}
	
	@GetMapping
	public ResponseEntity<?> findAll() {
		List<FairTO> fairs = fairRepository.findAll();
		fairs.sort((f1, f2) -> f1.getSiteName().compareTo(f2.getSiteName()));
		return new ResponseEntity<>(fairs , HttpStatus.OK);
	}	
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<FairDTO> findFairIdCustomer(@PathVariable Integer id) {
		if(fairRepository.findById(id).isPresent()) {			
			FairTO fair = fairRepository.findById(id).get();				
			FairDTO fairResponse = new FairDTO(fair.getSiteName(), fair.getDescription(), fair.getAddress(), fair.getCity(), fair.getUf(), fair.getDayWeek());
			
			Set<CustomerForm> customers = new CustomerForm().convert(fair.getCustomers());		
			
			List<CustomerForm> convertedList = customers.stream().collect(Collectors.toList());
			convertedList.sort((c1, c2) -> c1.getName().compareTo(c2.getName()));
			fairResponse.setCustomers(convertedList);	
			
			return new ResponseEntity<FairDTO>(fairResponse, HttpStatus.OK);
		}			
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);		
	}	
	
	@GetMapping(value = "/search/")
	public ResponseEntity<?> search(@RequestParam String parameter) {
		try {
		List<FairTO> filterListSiteName = fairRepository.findBySiteNameIsContaining(parameter).stream().filter(fair -> fair.getSiteName().contains(parameter)).collect(Collectors.toList());		
		if(!filterListSiteName.isEmpty()) 
			return new ResponseEntity<>(filterListSiteName, HttpStatus.OK);
		
		List<FairTO> filterListAddress = fairRepository.findByAddressIsContaining(parameter).stream().filter(fair -> fair.getAddress().contains(parameter)).collect(Collectors.toList());
		if(!filterListAddress.isEmpty()) 
			return new ResponseEntity<>(filterListAddress, HttpStatus.OK); 		
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@PostMapping(value = "/newfair")
	@Description(value = "")
	public ResponseEntity<?> newFair(@RequestBody CustomerFairForm cfForm) {
		try {
			Optional<CustomerTO> client = customerRepository.findById(cfForm.getCustomerId());
			CustomerTO customer = client.get();
			
			Set<FairTO> fairs = new TreeSet<FairTO>();
			fairs = customer.getFairs();		
			
			for(br.com.suafeira.to.dto.handler.FairForm handler : cfForm.getIdsFair()) {
				Optional<FairTO> fair = fairRepository.findById(handler.getIdFair());
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
	public ResponseEntity<?> removeFair(@RequestParam Integer customerId, @RequestParam Integer fairId) {
		try {
			Optional<CustomerTO> client = customerRepository.findById(customerId);
			
			if(client.isPresent()) {
				CustomerTO customer = client.get();
				
				Set<FairTO> fairs = new TreeSet<FairTO>();
				fairs = customer.getFairs();			
				
				Optional<FairTO> fair = fairRepository.findById(fairId);
				
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
			Optional<CustomerTO> client = customerRepository.findByEmail(form.getEmail());
			if(client.isPresent()) {
				CustomerTO customer = client.get();
				
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
	