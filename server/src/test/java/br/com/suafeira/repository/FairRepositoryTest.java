package br.com.suafeira.repository;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class FairRepositoryTest {

	@BeforeAll
	public void setup() {
		
	}

	@Test
	public void notShouldLoadAnCustomerByEmail() {
		
	}

}
