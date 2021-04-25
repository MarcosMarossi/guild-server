package br.com.suafeira.repository;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.suafeira.to.CustomerTO;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class CustomerRepositoryTest {
	
	private CustomerTO customer;

	@Autowired
	private CustomerRepository repository;

	@Before
	public void setupBefore() {
		customer = new CustomerTO("Develop", "19996557255", "develop@test.com", "1234");
		repository.save(customer);
	}
	
	@After
	public void setupAfter() {
		repository.delete(customer);
	}
	
	@Test
	public void shouldLoadCustomerById() {
		Integer id = 3;
		Optional<CustomerTO> optCustomer = repository.findById(id);

		CustomerTO customerTO = optCustomer.get();

		assertTrue(optCustomer.isPresent());
		assertEquals(3, customerTO.getId());
	}
	
	@Test
	public void shouldLoadCustomerByEmail() {
		String email = "develop@test.com";
		Optional<CustomerTO> optCustomer = repository.findByEmail(email);
		
		CustomerTO customerTO = optCustomer.get();
		
		assertTrue(optCustomer.isPresent());		
		assertEquals(email, customerTO.getEmail());
	}
	
	@Test
	public void shouldLoadCustomerByName() {
		String name = "Develop";
		Optional<CustomerTO> optCustomer = repository.findByName(name);

		CustomerTO customerTO = optCustomer.get();
		
		assertTrue(optCustomer.isPresent());
		assertEquals(name, customerTO.getName());
	}
	
	@Test
	public void shouldLoadAllCustomers() {
		List<CustomerTO> customers = repository.findAll();

		assertTrue(customers.size() > 0);
		assertEquals(1, customers.size());
	}

}
