package br.com.suafeira.repository;

import static org.junit.Assert.assertFalse;
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

import br.com.suafeira.to.FairTO;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class FairRepositoryTest {

	private FairTO greenFair;
	private FairTO redFair;
	
	@Autowired
	private FairRepository repository;
	
	@Before
	public void setupBefore() {		
		greenFair = new FairTO("Green Site", "We sell flowers", "Five Street", "São Paulo", "SP", "Monday",
				23.5484524, 45.4552484);
		redFair = new FairTO("Red Site", "We sell flowers", "One Street", "São Paulo", "SP", "Monday",
				23.5484524, 45.4552484);
		
		repository.save(greenFair);
		repository.save(redFair);
	}
	
	@After
	public void setupAfter() {		
		repository.delete(greenFair);
		repository.delete(redFair);
	}
	
	@Test
	public void shouldLoadFairById() {
		Integer id = 1;
		Optional<FairTO> optFair = repository.findById(id);

		FairTO fairTO = optFair.get();

		assertTrue(optFair.isPresent());
		assertEquals(1, fairTO.getId());
	}

	@Test
	public void shouldLoadFairBySiteNameIsContaining() {
		String partialName = "Green";
		List<FairTO> fairs = repository.findBySiteNameIsContaining(partialName);

		assertEquals(1, fairs.size());
		assertFalse(fairs.isEmpty());
	}

	@Test
	public void shouldLoadFairByAddressIsContaining() {
		String partialAdress = "Five";
		List<FairTO> fairs = repository.findByAddressIsContaining(partialAdress);

		assertEquals(1, fairs.size());
		assertFalse(fairs.isEmpty());
	}

	@Test
	public void shouldLoadAllFairs() {
		List<FairTO> fairs = repository.findAll();

		assertTrue(fairs.size() > 0);
		assertEquals(2, fairs.size());
		assertFalse(fairs.isEmpty());
	}

}
