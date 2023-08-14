package br.com.feira.guild.to.dto;

public class TokenDTO {
	
	private Integer id;
	private String type;
	private String token;
	
	public TokenDTO(String token, String type, Integer id) {
		this.token = token;
		this.type = type;
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
