package br.com.feira.guild.exceptions;

public class ValidateException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	
	public ValidateException(String message) 
	{
		super(message);
	}
}
