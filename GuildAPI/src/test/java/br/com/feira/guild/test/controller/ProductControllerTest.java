package br.com.feira.guild.test.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.feira.guild.controller.form.ProductForm;
import br.com.feira.guild.test.BaseTest;
import br.com.feira.guild.test.mount.PrepareData;
import br.com.feira.guild.to.Product;

public class ProductControllerTest extends BaseTest {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper mapper;

	@Value("${fair.api.keys}")
	protected String apiKey;

	@Test
	public void testIsUnauthorizedResquest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/products").accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isUnauthorized());
	}

	@Test
	public void testCreateProducts() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("API_KEY", apiKey);
		
		PrepareData prepareData = new PrepareData(mockMvc, mapper, headers);
		prepareData.createProduct(new ProductForm("Alho"));
	}

	@Test
	public void testFindProducts() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("API_KEY", apiKey);

		MvcResult andReturn = mockMvc
				.perform(MockMvcRequestBuilders.get("/products").headers(headers).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andReturn();

		String contentAsString = andReturn.getResponse().getContentAsString();
		List<Product> products = mapper.readValue(contentAsString, new TypeReference<List<Product>>() {
		});

		assertFalse(products.isEmpty());
		assertEquals(1, products.size());
	}

	@Test
	public void testDeleteProducts() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("API_KEY", apiKey);

		ProductForm form = new ProductForm();
		form.setName("Mandioca");

		MvcResult andReturn = mockMvc.perform(MockMvcRequestBuilders.post("/products").headers(headers)
				.content(mapper.writeValueAsString(form)).contentType(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isCreated()).andReturn();
		
		String contentAsString = andReturn.getResponse().getContentAsString();
		Product product = mapper.readValue(contentAsString, Product.class);

		mockMvc.perform(MockMvcRequestBuilders.delete("/products").headers(headers).param("id", product.getId().toString())).andDo(print())
				.andExpect(status().isNoContent());
	}

}