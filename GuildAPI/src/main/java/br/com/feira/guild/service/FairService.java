package br.com.feira.guild.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.feira.guild.exceptions.EntityNotFoundException;
import br.com.feira.guild.repository.FairRepository;
import br.com.feira.guild.to.Fair;
import br.com.feira.guild.to.dto.FairDTO;
import br.com.feira.guild.to.dto.handler.CustomerHandler;
import br.com.feira.guild.to.form.FairForm;

@Service
public class FairService {

	@Autowired
	private FairRepository fairRepository;

	public List<Fair> findAll() {
		List<Fair> fairs = fairRepository.findAll();

		fairs.sort((f1, f2) -> f1.getSiteName().compareTo(f2.getSiteName()));

		return fairs;
	}

	public Fair findById(Integer fairId) {
		Optional<Fair> fair = fairRepository.findById(fairId);

		if (!fair.isPresent()) {
			throw new EntityNotFoundException("Fair with id " + fairId + " not found");
		}

		return fair.get();
	}

	public Fair save(FairForm fair) {
		return fairRepository.save(fair.convertToFair());
	}

	public FairDTO findFairById(Integer id) {
		Optional<Fair> auxFair = fairRepository.findById(id);

		if (auxFair.isPresent()) {
			Fair fair = auxFair.get();

			FairDTO fairResponse = new FairDTO(fair.getSiteName(), fair.getDescription(), fair.getAddress(),
					fair.getCity(), fair.getUf(), fair.getDayWeek(), fair.getLatitude(), fair.getLongitude());

			Set<CustomerHandler> customers = new CustomerHandler().convert(fair.getCustomers());

			List<CustomerHandler> convertedList = customers.stream().collect(Collectors.toList());
			convertedList.sort((c1, c2) -> c1.getName().compareTo(c2.getName()));

			fairResponse.setCustomers(convertedList);

			return fairResponse;
		}

		throw new EntityNotFoundException("Fair with id " + id + " not found");
	}

	public List<Fair> search(String parameter) {
		List<Fair> searchList = new ArrayList<>();
		
		searchList = fairRepository.findBySiteNameIsContaining(parameter).stream()
				.filter(fair -> fair.getSiteName().contains(parameter)).collect(Collectors.toList());

		if (!searchList.isEmpty()) {
			return searchList;
		}
		
		searchList = fairRepository.findByAddressIsContaining(parameter).stream()
				.filter(fair -> fair.getAddress().contains(parameter)).collect(Collectors.toList());
		
		if (!searchList.isEmpty()) {
			return searchList;
		}
		
		return searchList;
		
	}

}
