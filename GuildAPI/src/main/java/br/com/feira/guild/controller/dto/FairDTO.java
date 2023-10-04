package br.com.feira.guild.controller.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class FairDTO {
	
	private String siteName;
	private String description;
	private String address;
	private String city;
	private String uf;
	private String dayWeek;
	private Double latitude;
	private Double longitude;
	private List<CustomerHandler> customers;
		
	public FairDTO(String siteName, String description, String address, String city, String uf, String dayWeek, Double latitude, Double longitude) {
		this.siteName = siteName;
		this.description = description;
		this.address = address;
		this.city = city;
		this.uf = uf;
		this.dayWeek = dayWeek;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
}
