package br.com.feira.guild.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import br.com.feira.guild.exceptions.EntityNotFoundException;
import br.com.feira.guild.service.FairService;
import br.com.feira.guild.to.Fair;
import br.com.feira.guild.to.dto.FairDTO;
import br.com.feira.guild.to.form.FairForm;

@RestController
@RequestMapping("/fairs")
public class FairController {
	
	@Autowired
	private FairService fairService;
	
	private static final Logger logger = LogManager.getLogger(FairController.class);
	
	@PostMapping
	public ResponseEntity<?> register(@RequestBody FairForm fair) {
		
		logger.info("Entering registration.");
		long initialTime = System.currentTimeMillis();
		
		try {
			Fair fairReturn = fairService.save(fair);
			return new ResponseEntity<>(fairReturn, HttpStatus.CREATED); 
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {
			long finalTime = System.currentTimeMillis();
			logger.info("Exiting registration in " + (finalTime - initialTime) + " s.");
		}
		
	}
	
	@GetMapping
	public ResponseEntity<?> findAll() {
		
		logger.info("Entering find all fairs.");
		long initialTime = System.currentTimeMillis();
		
		try {
			List<Fair> fairs = fairService.findAll();
			
			return new ResponseEntity<>(fairs , HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {
			long finalTime = System.currentTimeMillis();
			logger.info("Exiting find all fairs in " + (finalTime - initialTime) + " s.");
		}
		
	}	
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<FairDTO> findFairById(@PathVariable Integer id) {
		
		logger.info("Entering find by id fair.");
		long initialTime = System.currentTimeMillis();
		
		try {
			FairDTO fairResponse = fairService.findFairById(id);
			
			return new ResponseEntity<>(fairResponse , HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {
			long finalTime = System.currentTimeMillis();
			logger.info("Exiting find by id fair in " + (finalTime - initialTime) + " s.");
		}
		
	}	
	
	@GetMapping(value = "/search")
	public ResponseEntity<?> search(@RequestParam String parameter) {		
		
		logger.info("Entering search fair.");
		long initialTime = System.currentTimeMillis();
		
		try {
			List<Fair> fairs = fairService.search(parameter);			
			return new ResponseEntity<>(fairs , HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {
			long finalTime = System.currentTimeMillis();
			logger.info("Exiting search fair in " + (finalTime - initialTime) + " s.");
		}
		
	}
}
	