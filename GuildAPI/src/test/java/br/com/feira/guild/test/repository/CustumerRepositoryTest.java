package br.com.feira.guild.test.repository;

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

import br.com.feira.guild.repository.CustomerRepository;
import br.com.feira.guild.to.Customer;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class CustumerRepositoryTest {
	
	private Customer user;

	@Autowired
	private CustomerRepository repository;

	@Before
	public void setupBefore() {
		user = new Customer("Develop", "19996557255", "develop@test.com", "1234", null, null);
		repository.save(user);
	}
	
	@After
	public void setupAfter() {
		repository.delete(user);
	}
	
	@Test
	public void shouldLoaduserById() {
		Integer id = 1;
		Optional<Customer> optuser = repository.findById(id);

		Customer userTO = optuser.get();

		assertTrue(optuser.isPresent());
		assertEquals(1, userTO.getId());
	}
	
	@Test
	public void shouldLoaduserByEmail() {
		String email = "develop@test.com";
		Optional<Customer> optuser = repository.findByEmail(email);
		
		Customer userTO = optuser.get();
		
		assertTrue(optuser.isPresent());		
		assertEquals(email, userTO.getEmail());
	}
	
	@Test
	public void shouldLoaduserByName() {
		String name = "Develop";
		Optional<Customer> optuser = repository.findByName(name);

		Customer userTO = optuser.get();
		
		assertTrue(optuser.isPresent());
		assertEquals(name, userTO.getName());
	}
	
	@Test
	public void shouldLoadAllusers() {
		List<Customer> users = repository.findAll();

		assertTrue(users.size() > 0);
		assertEquals(1, users.size());
	}

}