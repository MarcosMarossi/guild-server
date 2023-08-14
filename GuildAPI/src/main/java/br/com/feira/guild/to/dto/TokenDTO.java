package br.com.feira.guild.to.dto;

public class TokenDTO {
	
	private Integer id;
	private String tipo;
	private String token;
	
	public TokenDTO(String token, String tipo, Integer id) {
		this.token = token;
		this.tipo = tipo;
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
