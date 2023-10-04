package br.com.feedback.controller.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class AssessmentForm {

	private String name;
	private String comment;
	private Integer idFair;
	private String serialNumber;
	
}
