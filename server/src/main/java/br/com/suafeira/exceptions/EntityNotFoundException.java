package br.com.suafeira.exceptions;

public class EntityNotFoundException extends BaseException {

	private static final long serialVersionUID = 1L;
	
	public EntityNotFoundException(String message) {
		super(message);
	}

}
