package br.com.suafeira.controller.advice;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.suafeira.to.form.ErrorForm;

@RestControllerAdvice
public class ControllerAdvice {
	
	@Autowired
	private MessageSource messageSource;
	
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public List<ErrorForm> handle(MethodArgumentNotValidException exception) {		
		List<ErrorForm> dto = new ArrayList<>();
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();			
		fieldErrors.forEach( e -> {			
			String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
			ErrorForm erro = new ErrorForm(e.getField(), mensagem);
			dto.add(erro);
		});
			
		return dto;
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<StandardError> database (ConstraintViolationException e, HttpServletRequest request){
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError error = new StandardError(Instant.now(), status.value() , e.getLocalizedMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(error);		
	}
}