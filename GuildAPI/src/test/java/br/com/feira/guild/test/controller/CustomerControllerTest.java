package br.com.feira.guild.test.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.feira.guild.controller.dto.CustomerDTO;
import br.com.feira.guild.controller.dto.TokenDTO;
import br.com.feira.guild.controller.form.AssociateForm;
import br.com.feira.guild.controller.form.FairForm;
import br.com.feira.guild.controller.form.ProductForm;
import br.com.feira.guild.controller.form.UpdateForm;
import br.com.feira.guild.test.BaseTest;
import br.com.feira.guild.test.mount.PrepareData;
import br.com.feira.guild.to.Fair;
import br.com.feira.guild.to.Product;
import jakarta.transaction.Transactional;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CustomerControllerTest extends BaseTest {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper mapper;

	@Value("${fair.api.keys}")
	protected String apiKey;

	private HttpHeaders headers;

	@Before
	public void setup() throws Exception {
		headers = new HttpHeaders();
		headers.add("API_KEY", apiKey);
	}

	@Test
	@Transactional
	public void testCreateCustomer() throws Exception {
		PrepareData prepareData = new PrepareData(mockMvc, mapper, headers);
		prepareData.authenticationWithDefaultUser();
	}

	@Test
	@Transactional
	public void testFindCustomerById() throws Exception {
		PrepareData prepareData = new PrepareData(mockMvc, mapper, headers);
		TokenDTO auth = prepareData.authenticationWithDefaultUser();

		headers.add("Bearer", auth.getToken());

		MvcResult andReturn = mockMvc.perform(MockMvcRequestBuilders.get("/customers/" + auth.getId()).headers(headers)
				.accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk()).andReturn();

		String contentAsString = andReturn.getResponse().getContentAsString();
		CustomerDTO customerDTO = mapper.readValue(contentAsString, CustomerDTO.class);

		List<Fair> fairs = new ArrayList<>(customerDTO.getFairs());
		List<Product> products = new ArrayList<>(customerDTO.getProducts());

		assertEquals("Admin", customerDTO.getName());
		assertEquals("19996000000", customerDTO.getWhatsapp());
		assertEquals("Green Site", fairs.get(0).getSiteName());
		assertEquals("Milho", products.get(0).getName());
	}

	@Test
	@Transactional
	public void testNewAssociation() throws Exception {
		PrepareData prepareData = new PrepareData(mockMvc, mapper, headers);
		TokenDTO auth = prepareData.authenticationWithDefaultUser();

		headers.add("Bearer", auth.getToken());

		// Associate with new product		
		Product product =  prepareData.createProduct(new ProductForm("Maçã"));

		AssociateForm associateForm = new AssociateForm();
		associateForm.setCustomerId(auth.getId());

		List<Integer> aux = new ArrayList<>();
		aux.add(product.getId());
		associateForm.setIdsProduct(aux);

		mockMvc.perform(MockMvcRequestBuilders.post("/customers/new-association").headers(headers)
				.content(mapper.writeValueAsString(associateForm)).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isCreated()).andReturn();
	}

	@Test
	@Transactional
	public void testUpdateCustomer() throws Exception {
		PrepareData prepareData = new PrepareData(mockMvc, mapper, headers);
		TokenDTO auth = prepareData.authenticationWithDefaultUser();

		headers.add("Bearer", auth.getToken());

		mockMvc.perform(MockMvcRequestBuilders.patch("/customers").headers(headers)
				.content(mapper.writeValueAsString(new UpdateForm(auth.getId(), "Admin", "admin@admin.com",
						"19996000000", "12345678", "123456789")))
				.contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isNoContent());
	}

	@Test
	@Transactional
	public void testUpdateCustomerWithBadCredencials() throws Exception {
		PrepareData prepareData = new PrepareData(mockMvc, mapper, headers);
		TokenDTO auth = prepareData.authenticationWithDefaultUser();

		headers.add("Bearer", auth.getToken());

		mockMvc.perform(MockMvcRequestBuilders.patch("/customers").headers(headers)
				.content(mapper.writeValueAsString(new UpdateForm(auth.getId(), "Admin", "admin@admin.com",
						"19996000000", "1234567888888", "123456789")))
				.contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest());
	}

	@Test
	@Transactional
	public void testDeleteFairAssociation() throws Exception {
		PrepareData prepareData = new PrepareData(mockMvc, mapper, headers);
		TokenDTO auth = prepareData.authenticationWithDefaultUser();

		headers.add("Bearer", auth.getToken());

		FairForm form = new FairForm("White Site", "We sell flowers", "Five Street", "São Paulo", "SP", "Monday",
				23.5484524, 45.4552484);

		Fair fair = prepareData.createFair(form);

		AssociateForm associateForm = new AssociateForm();
		associateForm.setCustomerId(auth.getId());

		List<Integer> aux = new ArrayList<>();
		aux.add(fair.getId());
		associateForm.setIdsFair(aux);

		mockMvc.perform(MockMvcRequestBuilders.post("/customers/new-association").headers(headers)
				.content(mapper.writeValueAsString(associateForm)).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isCreated()).andReturn();

		mockMvc.perform(
				MockMvcRequestBuilders.delete("/customers?customerId=" + auth.getId() + "&fairId=" + fair.getId())
						.headers(headers).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isNoContent());
	}

}