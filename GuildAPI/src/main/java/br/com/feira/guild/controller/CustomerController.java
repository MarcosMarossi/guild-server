package br.com.feira.guild.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.feira.guild.controller.dto.CodeDTO;
import br.com.feira.guild.controller.dto.CustomerDTO;
import br.com.feira.guild.controller.form.AssociateForm;
import br.com.feira.guild.controller.form.CustomerForm;
import br.com.feira.guild.controller.form.RecoveryForm;
import br.com.feira.guild.controller.form.CodeForm;
import br.com.feira.guild.controller.form.UpdateForm;
import br.com.feira.guild.service.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	private static final Logger logger = LogManager.getLogger(CustomerController.class);

	@PostMapping
	public ResponseEntity<?> register(@RequestBody CustomerForm register) {		
		logger.info("Entering registration.");
		long initialTime = System.currentTimeMillis();

		try {
			customerService.save(register);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {
			long finalTime = System.currentTimeMillis();
			logger.info("Exiting registration in " + (finalTime - initialTime) + " s.");
		}
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<CustomerDTO> findById(@PathVariable(value = "id") Integer id) {		
		logger.info("Entering find customer by id.");
		long initialTime = System.currentTimeMillis();

		try {
			CustomerDTO customer = customerService.findByIdWithFairAndProducts(id);
			return new ResponseEntity<CustomerDTO>(customer, HttpStatus.OK);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return new ResponseEntity<CustomerDTO>(HttpStatus.BAD_REQUEST);
		} finally {
			long finalTime = System.currentTimeMillis();
			logger.info("Exiting find customer by id in " + (finalTime - initialTime) + " s.");
		}
	}

	@PostMapping(value = "/new-association")
	public ResponseEntity<?> association(@RequestBody AssociateForm associateForm) {		
		logger.info("Entering new association.");
		long initialTime = System.currentTimeMillis();

		try {
			customerService.association(associateForm);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {
			long finalTime = System.currentTimeMillis();
			logger.info("Exiting new association in " + (finalTime - initialTime) + " s.");
		}
	}

	@DeleteMapping
	public ResponseEntity<?> deleteById(@RequestParam Integer customerId, @RequestParam Integer fairId) {		
		logger.info("Entering delete fair by id.");
		long initialTime = System.currentTimeMillis();
		
		try {
			customerService.deleteById(customerId, fairId);			
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {
			long finalTime = System.currentTimeMillis();
			logger.info("Exiting delete fair by id in " + (finalTime - initialTime) + " s.");
		}		
	}

	@PatchMapping
	public ResponseEntity<?> update(@RequestBody UpdateForm form) {		
		logger.info("Entering update fair.");
		long initialTime = System.currentTimeMillis();
		
		try {
			customerService.update(form);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {
			long finalTime = System.currentTimeMillis();
			logger.info("Exiting update fair in " + (finalTime - initialTime) + " s.");
		}		
	}
	
	@PutMapping(value = "send-code")
	public ResponseEntity<?> sendCode(@RequestBody CodeForm form) {
		logger.info("Entering send code.");
		long initialTime = System.currentTimeMillis();
		
		try {
			CodeDTO codeDTO = customerService.send(form);
			logger.info("Message sent successfully. Id: " + codeDTO.getSid());
			
			return new ResponseEntity<>(codeDTO, HttpStatus.CREATED); 
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {
			long finalTime = System.currentTimeMillis();
			logger.info("Exiting send code in " + (finalTime - initialTime) + " s.");
		}		
	}
	
	@PutMapping
	public ResponseEntity<?> changePassword(@RequestBody RecoveryForm form) {
		logger.info("Entering change user password.");
		long initialTime = System.currentTimeMillis();
		
		try {
			customerService.changePassword(form);			
			return new ResponseEntity<>(HttpStatus.CREATED); 
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {
			long finalTime = System.currentTimeMillis();
			logger.info("Exiting change user password in " + (finalTime - initialTime) + " s.");
		}		
	}
	
}