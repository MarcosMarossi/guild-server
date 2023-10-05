package br.com.feedback.controller.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class CompliantForm {
	
	private String reason;
	private String description;
	private Integer idFair;
	private String serialNumber;
	private String manufacturer;
	
}
