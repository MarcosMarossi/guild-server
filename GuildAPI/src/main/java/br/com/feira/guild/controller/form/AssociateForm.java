package br.com.feira.guild.controller.form;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class AssociateForm {

	private Integer customerId;
	private Integer fairId;
	private List<Integer> idsFair;
	private List<Integer> idsProduct;
	
}
