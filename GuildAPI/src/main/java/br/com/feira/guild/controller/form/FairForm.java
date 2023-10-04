package br.com.feira.guild.controller.form;

import br.com.feira.guild.to.Fair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class FairForm {

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
}
