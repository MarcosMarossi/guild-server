package br.com.suafeira.to.dto;

import java.util.List;
import java.util.Set;

import br.com.suafeira.to.FairTO;
import br.com.suafeira.to.ProductTO;

public class CustomerDTO {
	
	private String name;
	private String whatsapp;
	private String email;
	private Set<FairTO> fairs;
	private List<ProductTO> products;	
	
	public CustomerDTO(String name, String whatsapp, String email, Set<FairTO> fairs, List<ProductTO> products) {
		this.name = name;
		this.whatsapp = whatsapp;
		this.email = email;
		this.fairs = fairs;
		this.products = products;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWhatsapp() {
		return whatsapp;
	}
	public void setWhatsapp(String whatsapp) {
		this.whatsapp = whatsapp;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Set<FairTO> getFairs() {
		return fairs;
	}
	public void setFairs(Set<FairTO> fairs) {
		this.fairs = fairs;
	}
	public void setProducts(List<ProductTO> products) {
		this.products = products;
	}
	public List<ProductTO> getProducts() {
		return products;
	}
}
