package br.com.suafeira.controller;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.suafeira.service.CustomerService;
import br.com.suafeira.to.CustomerTO;
import br.com.suafeira.to.dto.CustomerDTO;
import br.com.suafeira.to.form.CustomerForm;

@RestController
@RequestMapping("/customers")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@PostMapping(value = "/insert")
	@Description(value = "Register new users with a some products and fairs.")
	public ResponseEntity<?> register(@RequestBody CustomerForm form) {
		try {
			customerService.insert(form);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = "/{id}")
	@Description(value = "Get consumer with Product and Fair list.")
	public ResponseEntity<CustomerDTO> getConsumerByFair(@PathVariable(value = "id") Integer id) {
		try {
			Optional<CustomerTO> client = customerService.findById(id);
			if (client.isPresent()) {
				CustomerDTO customer = customerService.getCustomer(client);
				return new ResponseEntity<CustomerDTO>(customer, HttpStatus.OK);
			}
			throw new EntityNotFoundException("It not found with Id= " + id);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<CustomerDTO>(HttpStatus.BAD_REQUEST);
		}
	}
}