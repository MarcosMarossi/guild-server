package br.com.feedback.controller.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentForm {

	private String name;
	private String comment;
	private Integer idFair;
	private String serialNumber;
	
}
