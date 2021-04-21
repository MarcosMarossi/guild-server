package br.com.suafeira.to.dto;

import java.util.List;

import br.com.suafeira.to.form.CustomerForm;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class FairDTO {
	
	private String siteName;
	private String description;
	private String address;
	private String city;
	private String uf;
	private String dayWeek;
	private List<CustomerForm> customers;
	
	public FairDTO(String siteName, String description, String address, String city, String uf, String dayWeek) {
		this.siteName = siteName;
		this.description = description;
		this.address = address;
		this.city = city;
		this.uf = uf;
		this.dayWeek = dayWeek;
	}	
}
