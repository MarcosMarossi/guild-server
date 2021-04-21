package br.com.suafeira.to.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {
	
	private String token;
	private String tipo;
	private Integer id;
	
}
