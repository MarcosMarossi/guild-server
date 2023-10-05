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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.feedback.controller.dto.AssessmentDTO;
import br.com.feedback.controller.form.AssessmentForm;
import br.com.feedback.service.AssessmentService;

@RestController
@RequestMapping(value = "/assessments")
public class AssessmentController {
	
	@Autowired
	private AssessmentService assessmentService;
	
	private static final Logger logger = LogManager.getLogger(AssessmentController.class);
	
	@PostMapping
	public ResponseEntity<?> register(@RequestBody AssessmentForm assessmentForm) {		
		long initialTime = System.currentTimeMillis();
		logger.info("Entering assessment registration.");
		
		try {
			assessmentService.save(assessmentForm);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {
			long finalTime = System.currentTimeMillis();
			logger.info("Exiting assessment registration in " + (finalTime - initialTime) + " s.");
		}
	}
	
	@GetMapping
	public ResponseEntity<?> findAssessmentByFairId(@RequestParam Integer idFair) {		
		long initialTime = System.currentTimeMillis();
		logger.info("Entering find assessments by idFair.");
		
		try {
			List<AssessmentDTO> dto = assessmentService.findAssessmentByFairId(idFair);
			return new ResponseEntity<>(dto, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} finally {
			long finalTime = System.currentTimeMillis();
			logger.info("Exiting find assessments by idFair " + (finalTime - initialTime) + " s.");
		}
	}

}
