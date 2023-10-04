package br.com.feira.guild.controller.dto;

import java.util.List;
import java.util.Set;

import br.com.feira.guild.to.Fair;
import br.com.feira.guild.to.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
	
	private String name;
	private String whatsapp;
	private String email;
	private Set<Fair> fairs;
	private List<Product> products;	
	
}
