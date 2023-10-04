package br.com.feira.guild.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {
	
	private Integer id;
	private String type;
	private String token;
	
	public TokenDTO(String token, String type, Integer id) {
		this.token = token;
		this.type = type;
		this.id = id;
	}
	
}
