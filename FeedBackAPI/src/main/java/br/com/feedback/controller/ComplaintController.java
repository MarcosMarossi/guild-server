package br.com.feedback.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.feedback.controller.form.CompliantForm;
import br.com.feedback.service.ComplaintService;
import br.com.feedback.to.Complaint;

@RestController
@RequestMapping(value = "/complaints")
public class ComplaintController {
	
	private static final Logger logger = LogManager.getLogger(AssessmentController.class);

	@Autowired
	private ComplaintService complaintService;
	
	@PostMapping
	public ResponseEntity<?> register(@RequestBody CompliantForm register) {		
		long initialTime = System.currentTimeMillis();
		logger.info("Entering compliant registration.");
		
		try {
			complaintService.save(register);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {
			long finalTime = System.currentTimeMillis();
			logger.info("Exiting assessment registration in " + (finalTime - initialTime) + " s.");
		}
	}
	
	@GetMapping
	public ResponseEntity<?> findAll() {		
		long initialTime = System.currentTimeMillis();
		logger.info("Entering find all assessments.");
		
		try {
			List<Complaint> dto = complaintService.findAll();
			return new ResponseEntity<>(dto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {
			long finalTime = System.currentTimeMillis();
			logger.info("Exiting find all assessments in " + (finalTime - initialTime) + " s.");
		}
	}

}
