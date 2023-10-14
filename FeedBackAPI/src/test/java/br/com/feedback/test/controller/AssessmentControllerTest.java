package br.com.feedback.test.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.type.TypeReference;

import br.com.feedback.controller.dto.AssessmentDTO;
import br.com.feedback.controller.form.AssessmentForm;
import br.com.feedback.test.BaseTest;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AssessmentControllerTest extends BaseTest {

	private HttpHeaders headers;

	@Before
	public void setup() throws Exception {
		headers = new HttpHeaders();
		headers.add("FEEDBACK_API_KEY", apiKey);
	}

	@Test
	public void testIsUnauthorizedResquest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/assessments").accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isUnauthorized());
	}

	@Test
	public void testCreateAssessment() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/assessments").headers(headers)
				.content(mapper.writeValueAsString(
						new AssessmentForm("Carlos", "Gostei da feira", 1, "SEFW4SF4WF54FS.SEFWEF4")))
				.contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isCreated());
	}

	@Test
	public void testFindAssessmentsByFairId() throws Exception {
		MvcResult andReturn = mockMvc.perform(MockMvcRequestBuilders.get("/assessments").param("idFair", "1")
				.headers(headers).accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
				.andReturn();

		String contentAsString = andReturn.getResponse().getContentAsString();
		List<AssessmentDTO> assessments = mapper.readValue(contentAsString, new TypeReference<List<AssessmentDTO>>() {
		});

		assertFalse(assessments.isEmpty());
		assertEquals(1, assessments.size());
	}

	@Test
	public void testRemoveAssessment() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/assessments").headers(headers).param("id", "1"))
				.andDo(print()).andExpect(status().isNoContent());
	}
}
