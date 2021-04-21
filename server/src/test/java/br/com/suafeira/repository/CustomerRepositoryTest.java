package br.com.suafeira.repository;

import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.suafeira.to.CustomerTO;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class CustomerRepositoryTest {

	@Autowired
	private CustomerRepository repository;

	@Autowired
	private TestEntityManager em;

	@BeforeAll
	public void setup() {
		CustomerTO customer = new CustomerTO("Marcos Marossi", "19996127100", "marcostest@test.com", "1234");
		
		em.persist(customer);
	}

	@Test
	public void notShouldLoadAnCustomerByEmail() {
		String email = "emailnaoexistente@test.com";
		Optional<CustomerTO> customer = repository.findByEmail(email);

		assertTrue(customer.isEmpty());
	}

}
