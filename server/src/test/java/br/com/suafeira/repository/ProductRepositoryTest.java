package br.com.suafeira.repository;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.suafeira.to.ProductTO;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class ProductRepositoryTest {

	@Autowired
	private ProductRepository repository;

	@Autowired
	private TestEntityManager em;

	@BeforeAll
	public void setup() {
		ProductTO product = new ProductTO("Apple");
		em.persist(product);
	}

	@Test
	public void notShouldLoadAnCustomerByEmail() {
		 List<ProductTO> products = repository.findAll();

		assertTrue(products.isEmpty());
	}

}
