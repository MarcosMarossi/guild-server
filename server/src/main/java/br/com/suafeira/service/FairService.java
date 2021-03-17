package br.com.suafeira.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.suafeira.exceptions.CustomerException;
import br.com.suafeira.exceptions.EntityNotFoundException;
import br.com.suafeira.repository.CustomerRepository;
import br.com.suafeira.repository.FairRepository;
import br.com.suafeira.to.CustomerTO;
import br.com.suafeira.to.FairTO;
import br.com.suafeira.to.dto.FairDTO;
import br.com.suafeira.to.dto.handler.CustomerForm;
import br.com.suafeira.to.form.CustomerFairForm;
import br.com.suafeira.to.form.UpdateForm;

@Service
public class FairService {

	@Autowired
	private FairRepository fairRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	public FairDTO findFairByIdConsumer(Integer id) {
		FairTO fair = fairRepository.findById(id).get();				
		FairDTO fairResponse = new FairDTO(fair.getSiteName(), fair.getDescription(), fair.getAddress(), fair.getCity(), fair.getUf(), fair.getDayWeek());
		
		Set<CustomerForm> customers = new CustomerForm().convert(fair.getCustomers());		
		
		List<CustomerForm> convertedList = customers.stream().collect(Collectors.toList());
		convertedList.sort((c1, c2) -> c1.getName().compareTo(c2.getName()));
		fairResponse.setCustomers(convertedList);
		return fairResponse;
	}	
	
	public List<FairTO> findBySiteName(String parameter) {
		return fairRepository.findBySiteNameIsContaining(parameter)
							.stream()
							.filter(fair -> fair.getSiteName().contains(parameter))
							.collect(Collectors.toList());
	}
	
	public List<FairTO> findByAddress(String parameter) {
		return fairRepository.findByAddressIsContaining(parameter)
							.stream()
							.filter(fair -> fair.getAddress().contains(parameter))
							.collect(Collectors.toList());
	}
	
	public void saveFair(CustomerFairForm cfForm) {
		Optional<CustomerTO> client = customerRepository.findById(cfForm.getCustomerId());
		CustomerTO customer = client.get();

		Set<FairTO> fairs = new TreeSet<FairTO>();
		fairs = customer.getFairs();

		for (br.com.suafeira.to.dto.handler.FairForm handler : cfForm.getIdsFair()) {
			Optional<FairTO> fair = fairRepository.findById(handler.getIdFair());
			fairs.add(fair.get());
		}
		customer.setFairs(fairs);
		customerRepository.save(customer);
	}
	
	public void deleteFair(Integer fairId, Optional<CustomerTO> client) {
		CustomerTO customer = client.get();

		Set<FairTO> fairs = new TreeSet<FairTO>();
		fairs = customer.getFairs();

		Optional<FairTO> fair = fairRepository.findById(fairId);

		if (!fair.isPresent()) {
			throw new EntityNotFoundException("Faid Id " + fairId + " not found.");
		}

		fairs.remove(fair.get());

		customer.setFairs(fairs);
		customerRepository.save(customer);
	}
	
	public void updateFair(UpdateForm form, Optional<CustomerTO> client) {
		CustomerTO customer = client.get();

		if (!form.isValidWhatsApp()) {
			throw new CustomerException("The WhatsApp informed is invalid!");
		}

		if (!form.isValidPassword()) {
			throw new CustomerException("The password informed is invalid!");
		}
		
		customer.setWhatsapp(form.getWhatsapp());
		String registerPassword = new BCryptPasswordEncoder().encode(form.getCustomerNewPassword());
		customer.setCustomerPassword(registerPassword);

		customerRepository.save(customer);
	}
}
