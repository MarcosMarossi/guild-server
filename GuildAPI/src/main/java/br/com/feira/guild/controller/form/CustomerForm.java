package br.com.feira.guild.controller.form;

import java.util.List;

import br.com.feira.guild.controller.dto.FairHandler;
import br.com.feira.guild.controller.dto.ProductHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerForm {

	private String name;
	private String email;
	private String whatsapp;
	private String customerPassword;
	private List<ProductHandler> idsProduct;
	private List<FairHandler> idsFair;
	
}
