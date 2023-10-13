package br.com.feira.guild.test.mount;

import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.feira.guild.controller.dto.FairHandler;
import br.com.feira.guild.controller.dto.ProductHandler;
import br.com.feira.guild.controller.dto.TokenDTO;
import br.com.feira.guild.controller.form.CustomerForm;
import br.com.feira.guild.controller.form.FairForm;
import br.com.feira.guild.controller.form.LoginForm;
import br.com.feira.guild.controller.form.ProductForm;
import br.com.feira.guild.to.Fair;
import br.com.feira.guild.to.Product;

public class PrepareData {

	private MockMvc mockMvc;
	private ObjectMapper mapper;
	private HttpHeaders headers;

	public PrepareData(MockMvc mockMvc, ObjectMapper mapper, HttpHeaders headers) {
		this.mockMvc = mockMvc;
		this.mapper = mapper;
		this.headers = headers;
	}

	public void createDefaultCustomer() {
		try {
			Fair fair = createFair(new FairForm("Green Site", "We sell flowers", "Five Street", "Sao Paulo", "SP",
					"Monday", 23.5484524, 45.4552484));
			
			Product product = createProduct(new ProductForm("Milho"));

			List<ProductHandler> productHandlers = new ArrayList<>();
			productHandlers.add(new ProductHandler(product.getId()));

			List<FairHandler> fairHandlers = new ArrayList<>();
			fairHandlers.add(new FairHandler(fair.getId()));

			mockMvc.perform(MockMvcRequestBuilders.post("/customers").headers(headers)
					.content(mapper.writeValueAsString(new CustomerForm("Admin", "admin@admin.com", "19996000000",
							"12345678", productHandlers, fairHandlers)))
					.contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isCreated());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	public TokenDTO authenticationWithDefaultUser() {
		try {
			createDefaultCustomer();

			MvcResult andReturn = mockMvc
					.perform(MockMvcRequestBuilders.post("/auth").headers(headers)
							.content(mapper.writeValueAsString(new LoginForm("admin@admin.com", "12345678")))
							.contentType(MediaType.APPLICATION_JSON))
					.andDo(print()).andExpect(status().isOk()).andReturn();

			String contentAsString = andReturn.getResponse().getContentAsString();
			TokenDTO tokenDTO = mapper.readValue(contentAsString, TokenDTO.class);

			return tokenDTO;
		} catch (Exception e) {
			fail(e.getMessage());
		}

		return null;
	}

	public Fair createFair(FairForm form) {
		try {
			MvcResult andReturn = mockMvc
					.perform(MockMvcRequestBuilders.post("/fairs").headers(headers)
							.content(mapper.writeValueAsString(form)).contentType(MediaType.APPLICATION_JSON))
					.andDo(print()).andExpect(status().isCreated()).andReturn();

			String contentAsString = andReturn.getResponse().getContentAsString();

			return mapper.readValue(contentAsString, Fair.class);
		} catch (Exception e) {
			fail(e.getMessage());
		}

		return null;
	}

	public Product createProduct(ProductForm form) {
		try {
			MvcResult andReturn = mockMvc
					.perform(MockMvcRequestBuilders.post("/products").headers(headers)
							.content(mapper.writeValueAsString(form)).contentType(MediaType.APPLICATION_JSON))
					.andDo(print()).andExpect(status().isCreated()).andReturn();

			String contentAsString = andReturn.getResponse().getContentAsString();

			return mapper.readValue(contentAsString, Product.class);
		} catch (Exception e) {
			fail(e.getMessage());
		}

		return null;
	}
}
