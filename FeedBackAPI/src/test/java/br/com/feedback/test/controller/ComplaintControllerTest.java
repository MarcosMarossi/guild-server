package br.com.feedback.test.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.type.TypeReference;

import br.com.feedback.controller.form.CompliantForm;
import br.com.feedback.test.BaseTest;
import br.com.feedback.to.Complaint;

public class ComplaintControllerTest extends BaseTest {

	private HttpHeaders headers;

	@Before
	public void setup() throws Exception {
		headers = new HttpHeaders();
		headers.add("FEEDBACK_API_KEY", apiKey);
	}

	@Test
	public void testIsUnauthorizedResquest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/complaints").accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isUnauthorized());
	}

	@Test
	public void testCreateAssessment() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/complaints").headers(headers)
				.content(mapper.writeValueAsString(new CompliantForm("Feira imprópria",
						"Problema com a descrição da feira, pois tem conteúdo impróprio.", 1, "SEFW4SF4WF54FS.SEFWEF4",
						"Android")))
				.contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isCreated());
	}

	@Test
	public void testFindComplaints() throws Exception {
		MvcResult andReturn = mockMvc
				.perform(MockMvcRequestBuilders.get("/complaints").headers(headers).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andReturn();

		String contentAsString = andReturn.getResponse().getContentAsString();
		List<Complaint> assessments = mapper.readValue(contentAsString, new TypeReference<List<Complaint>>() {
		});

		assertFalse(assessments.isEmpty());
		assertEquals(1, assessments.size());
	}
}
