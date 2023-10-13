package br.com.feira.guild.test.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Before;
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

import br.com.feira.guild.controller.dto.FairDTO;
import br.com.feira.guild.controller.form.FairForm;
import br.com.feira.guild.test.BaseTest;
import br.com.feira.guild.test.mount.PrepareData;
import br.com.feira.guild.to.Fair;

public class FairControllerTest extends BaseTest {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper mapper;

	@Value("${fair.api.keys}")
	protected String apiKey;

	private HttpHeaders headers;
	
	private static Fair tmpFair;

	@Before
	public void setup() {
		headers = new HttpHeaders();
		headers.add("API_KEY", apiKey);
	}

	@Test
	public void testIsUnauthorizedResquest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/fairs").accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isUnauthorized());
	}

	@Test
	public void testCreateFairs() throws Exception {
		FairForm form = new FairForm(2, "Green Site", "We sell flowers", "Five Street", "São Paulo", "SP", "Monday",
				23.5484524, 45.4552484);
		
		PrepareData prepareData = new PrepareData(mockMvc, mapper, headers);
		prepareData.createFair(form);
				
		MvcResult andReturn = mockMvc
				.perform(MockMvcRequestBuilders.post("/fairs").headers(headers).content(mapper.writeValueAsString(form))
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isCreated()).andReturn();
		
		String contentAsString = andReturn.getResponse().getContentAsString();
		tmpFair = mapper.readValue(contentAsString, Fair.class);		
	}

	@Test
	public void testFindFairs() throws Exception {
		MvcResult andReturn = mockMvc
				.perform(MockMvcRequestBuilders.get("/fairs").headers(headers).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andReturn();

		String contentAsString = andReturn.getResponse().getContentAsString();
		List<Fair> fairs = mapper.readValue(contentAsString, new TypeReference<List<Fair>>() {
		});

		assertFalse(fairs.isEmpty());
		assertEquals(1, fairs.size());
	}

	@Test
	public void testFindFairsById() throws Exception {
		MvcResult andReturn = mockMvc
				.perform(MockMvcRequestBuilders.get("/fairs/" + tmpFair.getId()).headers(headers).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andReturn();

		String contentAsString = andReturn.getResponse().getContentAsString();
		FairDTO fairDTO = mapper.readValue(contentAsString, FairDTO.class);

		assertEquals("Green Site", fairDTO.getSiteName());
		assertEquals(0, fairDTO.getCustomers().size());
	}

	@Test
	public void testFindFairsBySearch() throws Exception {
		MvcResult andReturn = mockMvc.perform(MockMvcRequestBuilders.get("/fairs/search")
				.param("parameter", "Green Site").headers(headers).accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk()).andReturn();

		String contentAsString = andReturn.getResponse().getContentAsString();
		List<Fair> fairs = mapper.readValue(contentAsString, new TypeReference<List<Fair>>() {
		});

		assertFalse(fairs.isEmpty());
		assertEquals(1, fairs.size());
		assertEquals("Green Site", fairs.get(0).getSiteName());
	}

	@Test
	public void testUpdate() throws Exception {
		MvcResult andReturn = mockMvc.perform(MockMvcRequestBuilders.get("/fairs/search")
				.param("parameter", "Green Site").headers(headers).accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk()).andReturn();

		String contentAsString = andReturn.getResponse().getContentAsString();
		List<Fair> fairs = mapper.readValue(contentAsString, new TypeReference<List<Fair>>() {
		});

		assertFalse(fairs.isEmpty());
		assertEquals(1, fairs.size());
		assertEquals("Green Site", fairs.get(0).getSiteName());
	}

	@Test
	public void testDeleteFairs() throws Exception {
		FairForm form = new FairForm("Red Site", "We sell flowers", "Five Street", "São Paulo", "SP", "Monday",
				23.5484524, 45.4552484);
		
		PrepareData prepareData = new PrepareData(mockMvc, mapper, headers);
		Fair fair = prepareData.createFair(form);

		mockMvc.perform(MockMvcRequestBuilders.delete("/fairs").headers(headers).param("id", fair.getId().toString()))
				.andDo(print()).andExpect(status().isNoContent());
	}

}