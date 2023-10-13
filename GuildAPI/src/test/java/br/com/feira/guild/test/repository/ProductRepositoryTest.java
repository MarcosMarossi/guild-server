package br.com.feira.guild.test.repository;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.feira.guild.repository.ProductRepository;
import br.com.feira.guild.to.Product;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductRepositoryTest {

	@Autowired
	private ProductRepository repository;
	
	private Product save;

	@Before
	public void setupBefore() {
		save = repository.save(new Product("Alho"));
	}

	@Test
	public void shouldCreateInvalidProduct() {
		assertThrows(RuntimeException.class, () -> repository.save(new Product("Alho")));
	}
	
	@Test
	public void shouldCreateProduct() {
		Product save = repository.save(new Product("Cebola"));
		assertEquals(4, save.getId());
	}

	@Test
	public void shouldLoadProductById() {
		Optional<Product> optProduct = repository.findById(save.getId());

		Product productTO = optProduct.get();

		assertTrue(optProduct.isPresent());
		assertEquals(save.getId(), productTO.getId());
	}

	@Test
	public void shouldLoadAllProducts() {
		List<Product> products = repository.findAll();

		assertTrue(products.size() > 0);
		assertEquals(1, products.size());
	}
	
	@Test
	public void shouldDeleteProduct() {
		try {
			repository.deleteById(5);
			assertTrue(true);
		} catch (Exception e) {
			assertTrue(false);
		}
	}
}