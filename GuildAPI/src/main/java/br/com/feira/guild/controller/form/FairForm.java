package br.com.feira.guild.controller.form;

import br.com.feira.guild.to.Fair;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class FairForm {

	private Integer id;
	private String siteName;
	private String description;
	private String address;
	private String city;
	private String uf;
	private String dayWeek;
	private Double latitude;
	private Double longitude;
	
	public Fair convertToFair() {
		return new Fair(siteName, description, address, city, uf, dayWeek, latitude, longitude);
	}

	public FairForm(String siteName, String description, String address, String city, String uf, String dayWeek,
			Double latitude, Double longitude) {
		super();
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
