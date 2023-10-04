package br.com.feedback.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.feedback.controller.form.CompliantForm;
import br.com.feedback.repository.ComplaintRepository;
import br.com.feedback.to.Complaint;

@Service
public class ComplaintService {

	@Autowired
	private ComplaintRepository complaintRepository;

	public void save(CompliantForm register) {
		Complaint to = new Complaint();
		to.setDescription(register.getDescription());
		to.setIdFair(register.getIdFair());
		to.setReason(register.getReason());
		to.setSerialNumber(register.getSerialNumber());

		complaintRepository.save(to);
	}

	public List<Complaint> findAll() {
		return complaintRepository.findAll();
	}

}
