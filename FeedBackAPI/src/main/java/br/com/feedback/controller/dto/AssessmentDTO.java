package br.com.feedback.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentDTO {

	private String comment;
	private String name;
	private Integer id;
	private String serialNumber;

}
