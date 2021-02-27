package br.com.suafeira.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.com.suafeira.to.CustomerTO;

@Service
public interface CustomerRepository extends JpaRepository<CustomerTO, Integer> {

	Optional<CustomerTO> findByEmail(String email);

	Optional<CustomerTO> findById(Long id);
	
	Optional<CustomerTO> findByName(String name);

}
