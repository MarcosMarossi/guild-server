package br.com.feira.guild.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.com.feira.guild.to.Customer;

@Service
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	Optional<Customer> findByEmail(String email);

	Optional<Customer> findById(Long id);
	
	Optional<Customer> findByName(String name);

}
