package br.com.suafeira.to.dto;

import java.util.List;
import java.util.Set;

import br.com.suafeira.to.FairTO;
import br.com.suafeira.to.ProductTO;
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
	private Set<FairTO> fairs;
	private List<ProductTO> products;	
	
}
