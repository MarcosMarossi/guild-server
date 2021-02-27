package br.com.suafeira.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.suafeira.to.FairTO;

public interface FairRepository extends JpaRepository<FairTO, Integer> {

	Optional<FairTO> findById(Integer id);
	
	List<FairTO> findBySiteNameIsContaining(String siteName);	
	
	List<FairTO> findByAddressIsContaining(String address);

}
