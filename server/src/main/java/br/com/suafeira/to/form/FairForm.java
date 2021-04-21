package br.com.suafeira.to.form;

import br.com.suafeira.to.FairTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class FairForm {

	private Integer idFair;
	private String siteName;
	private String description;
	private String address;
	private String city;
	private String uf;
	private String dayWeek;
	private Double latitude;
	private Double longitude;

	public FairTO convertToFair() {
		return new FairTO(siteName, description, address, city, uf, dayWeek, latitude, longitude);
	}	
}
