package br.com.feira.guild.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.feira.guild.controller.dto.CustomerHandler;
import br.com.feira.guild.controller.dto.FairDTO;
import br.com.feira.guild.controller.form.FairForm;
import br.com.feira.guild.exceptions.EntityNotFoundException;
import br.com.feira.guild.repository.FairRepository;
import br.com.feira.guild.to.Fair;

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

	public void update(FairForm form) {
		Optional<Fair> auxFair = fairRepository.findById(form.getId());
		
		if(auxFair.isPresent()) {
			Fair fair = auxFair.get();
			
			Fair updatedFair = new Fair();
			
			updatedFair.setLatitude(form.getLatitude() != null ? form.getLatitude() : fair.getLatitude());
			updatedFair.setLongitude(form.getLongitude() != null ? form.getLongitude() : fair.getLongitude());
			updatedFair.setUf(form.getUf() != null ? form.getUf() : fair.getUf());
			updatedFair.setSiteName(form.getSiteName() != null ? form.getSiteName() : fair.getSiteName());
			updatedFair.setDescription(form.getDescription() != null ? form.getDescription() : fair.getDescription());
			updatedFair.setDayWeek(form.getDayWeek() != null ? form.getDayWeek() : fair.getDayWeek());
			updatedFair.setCity(form.getCity() != null ? form.getCity() : fair.getCity());
			updatedFair.setAddress(form.getAddress() != null ? form.getAddress() : fair.getAddress());			
			updatedFair.setId(form.getId());
			
			fairRepository.save(updatedFair);
		} else {
			throw new EntityNotFoundException("Fair with id " + form.getId() + " not found");
		}
	}

	public void delete(Integer id) {
		Optional<Fair> auxFair = fairRepository.findById(id);
		
		if(auxFair.isPresent()) {
			Fair fair = auxFair.get();
			fairRepository.delete(fair);
		} else {
			throw new EntityNotFoundException("Fair with id " + id + " not found");
		}
	}
	
}
