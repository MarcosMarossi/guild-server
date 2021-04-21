package br.com.suafeira.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.suafeira.service.CustomerService;
import br.com.suafeira.service.FairService;
import br.com.suafeira.to.CustomerTO;
import br.com.suafeira.to.FairTO;
import br.com.suafeira.to.dto.FairDTO;
import br.com.suafeira.to.form.CustomerFairForm;
import br.com.suafeira.to.form.FairForm;
import br.com.suafeira.to.form.UpdateForm;

@RestController
@RequestMapping("/fairs")
public class FairController {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private FairService fairService;

	@PostMapping
	public ResponseEntity<?> register(@RequestBody FairForm fair) {
		FairTO fairReturn = fairService.save(fair.convertToFair());
		return new ResponseEntity<>(fairReturn.getId(), HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<?> findAll() {
		List<FairTO> fairs = fairService.findAll();
		fairs.sort((f1, f2) -> f1.getSiteName().compareTo(f2.getSiteName()));
		return new ResponseEntity<>(fairs, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<FairDTO> findFairIdCustomer(@PathVariable Integer id) {
		if (fairService.findById(id).isPresent()) {
			FairDTO fairResponse = fairService.findFairByIdConsumer(id);
			return new ResponseEntity<FairDTO>(fairResponse, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping(value = "/search/")
	public ResponseEntity<?> search(@RequestParam String parameter) {
		try {
			List<FairTO> filterListSiteName = fairService.findBySiteName(parameter);
			if (!filterListSiteName.isEmpty())
				return new ResponseEntity<>(filterListSiteName, HttpStatus.OK);

			List<FairTO> filterListAddress = fairService.findByAddress(parameter);
			if (!filterListAddress.isEmpty())
				return new ResponseEntity<>(filterListAddress, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PostMapping(value = "/insert")
	@Description(value = "")
	public ResponseEntity<?> newFair(@RequestBody CustomerFairForm cfForm) {
		try {
			fairService.saveFair(cfForm);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping(value = "/delete")
	public ResponseEntity<?> removeFair(@RequestParam Integer customerId, @RequestParam Integer fairId) {
		try {
			Optional<CustomerTO> client = customerService.findById(customerId);
			if (client.isPresent()) {
				fairService.deleteFair(fairId, client);
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(value = "/update")
	public ResponseEntity<?> update(@RequestBody UpdateForm form) {
		try {
			Optional<CustomerTO> client = customerService.findByEmail(form.getEmail());
			if (client.isPresent()) {
				fairService.updateFair(form, client);
				return new ResponseEntity<>(HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}