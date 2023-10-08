package br.com.feedback.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.feedback.controller.dto.AssessmentDTO;
import br.com.feedback.controller.form.AssessmentForm;
import br.com.feedback.exceptions.EntityNotFoundException;
import br.com.feedback.repository.AssessmentRepository;
import br.com.feedback.to.Assessment;

@Service
public class AssessmentService {
	
	@Autowired
	private AssessmentRepository assessmentRepository;

	public void save(AssessmentForm register) {
		Assessment to = new Assessment();
		to.setComment(register.getComment());
		to.setIdFair(register.getIdFair());
		to.setName(register.getName());
		to.setSerialNumber(register.getSerialNumber());
		
		assessmentRepository.save(to);
	}

	public List<AssessmentDTO> findAll() {
		List<Assessment> assessments = assessmentRepository.findAll();
		
		List<AssessmentDTO> dtoList = convert2DTO(assessments);	
		
		return dtoList;
	}

	public List<AssessmentDTO> findAssessmentByFairId(Integer idFair) {
		List<Assessment> assessments = assessmentRepository.findByIdFair(idFair);
		
		List<AssessmentDTO> result = convert2DTO(assessments);
		
		return result;
	}

	private List<AssessmentDTO> convert2DTO(List<Assessment> assessments) {
		List<AssessmentDTO> dtoList = new ArrayList<>(); 
		
		for (Assessment assessment : assessments) {
			AssessmentDTO dto = new AssessmentDTO(assessment.getComment(), assessment.getName(), assessment.getId(), assessment.getSerialNumber());	
			dtoList.add(dto);
		}
		
		return dtoList;
	}

	public void deleteById(Integer id) {
		Optional<Assessment> optAssessment = assessmentRepository.findById(id);
		
		if(optAssessment.isPresent()) {
			assessmentRepository.deleteById(id);
		} else {
			throw new EntityNotFoundException("Rate with id " + id + " not found");
		}		
	}

}
