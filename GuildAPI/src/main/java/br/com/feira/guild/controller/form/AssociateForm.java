package br.com.feira.guild.controller.form;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssociateForm {

	private Integer customerId;
	private List<Integer> idsFair;
	private List<Integer> idsProduct;
	
}
