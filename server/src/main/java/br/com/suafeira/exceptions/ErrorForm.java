package br.com.suafeira.exceptions;

public class ErrorForm {
	
	private String erro;
	private String campo;
	
	public ErrorForm(String erro, String campo) {
		super();
		this.erro = erro;
		this.campo = campo;
	}

	public String getErro() {
		return erro;
	}
	
	public String getCampo() {
		return campo;
	}	
}
