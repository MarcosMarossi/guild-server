package br.com.feira.guild.exceptions;

public class CustomerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CustomerException(String msg) {
		super(msg);
	}
}
