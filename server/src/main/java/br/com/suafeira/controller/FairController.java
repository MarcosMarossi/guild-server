package br.com.suafeira.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.suafeira.repository.FairRepository;
import br.com.suafeira.to.Fair;
import br.com.suafeira.to.dto.FairDTO;
import br.com.suafeira.to.dto.handler.CustomerHandler;
import br.com.suafeira.to.form.FairForm;

@RestController
@RequestMapping("/fairs")
public class FairController {
	
	@Autowired
	private FairRepository fairRepository;
	
	@PostMapping
	public ResponseEntity<?> register(@RequestBody FairForm fair) {
		fairRepository.save(fair.convertToFair());
		return new ResponseEntity<>(HttpStatus.CREATED); 		
	}
	
	@GetMapping
	public ResponseEntity<?> findAll() {
		return new ResponseEntity<>(fairRepository.findAll(), HttpStatus.OK);
	}	
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<FairDTO> findFairIdCustomer(@PathVariable Integer id) {
		if(fairRepository.findById(id).isPresent()) {			
			Fair fair = fairRepository.findById(id).get();				
			FairDTO fairResponse = new FairDTO(fair.getSiteName(), fair.getDescription(), fair.getAddress(), fair.getCity(), fair.getUf(), fair.getDayWeek());
			CustomerHandler ch = new CustomerHandler();
			Set<CustomerHandler> customers = ch.convert(fair.getCustomers());				
			fairResponse.setCustomers(customers);			
			return new ResponseEntity<FairDTO>(fairResponse, HttpStatus.OK);
		}			
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);		
	}	
	
	@GetMapping(value = "/search/")
	public ResponseEntity<?> search(@RequestParam String parameter) {
		try {
		List<Fair> filterListSiteName = fairRepository.findBySiteNameIsContaining(parameter).stream().filter(fair -> fair.getSiteName().contains(parameter)).collect(Collectors.toList());		
		if(!filterListSiteName.isEmpty()) 
			return new ResponseEntity<>(filterListSiteName, HttpStatus.OK);
		
		List<Fair> filterListAddress = fairRepository.findByAddressIsContaining(parameter).stream().filter(fair -> fair.getAddress().contains(parameter)).collect(Collectors.toList());
		if(!filterListAddress.isEmpty()) 
			return new ResponseEntity<>(filterListAddress, HttpStatus.OK); 		
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
	