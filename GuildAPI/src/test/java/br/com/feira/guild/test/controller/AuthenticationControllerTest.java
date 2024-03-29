package br.com.feira.guild.test.controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.feira.guild.controller.dto.TokenDTO;
import br.com.feira.guild.controller.form.LoginForm;
import br.com.feira.guild.test.mount.PrepareData;
import jakarta.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthenticationControllerTest {
	
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
	public void testAuth() throws Exception {
		PrepareData prepareData = new PrepareData(mockMvc, mapper, headers);
		prepareData.createDefaultCustomer();

		MvcResult andReturn = mockMvc.perform(MockMvcRequestBuilders.post("/auth").headers(headers)
				.content(mapper.writeValueAsString(new LoginForm("admin@admin.com", "12345678")))
				.contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk()).andReturn();

		String contentAsString = andReturn.getResponse().getContentAsString();
		TokenDTO tokenDTO = mapper.readValue(contentAsString, TokenDTO.class);

		assertFalse(tokenDTO.getToken().isEmpty());
		assertTrue(tokenDTO.getType().equals("Bearer"));
	}

	@Test
	public void testInvalidCredentials() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/auth").headers(headers)
				.content(mapper.writeValueAsString(new LoginForm("admin@admin.com", "12345678")))
				.contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest());
	}
}
