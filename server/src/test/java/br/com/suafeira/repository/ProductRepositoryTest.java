package br.com.suafeira.repository;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.suafeira.to.ProductTO;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class ProductRepositoryTest {

	@Autowired
	private ProductRepository repository;

	@Before
	public void setup() {
		ProductTO product = new ProductTO("Apple");
		repository.save(product);
	}

	@Test
	public void shouldLoadProductById() {
		Integer id = 1;
		Optional<ProductTO> optProduct = repository.findById(id);

		ProductTO productTO = optProduct.get();

		assertTrue(optProduct.isPresent());
		assertEquals(1, productTO.getId());
	}

	@Test
	public void shouldLoadAllProducts() {
		List<ProductTO> products = repository.findAll();

		assertTrue(products.size() > 0);
		assertEquals(1, products.size());
	}

}
