package com.example.cbumanage.authentication.exceptions.handler;

import com.example.cbumanage.authentication.exceptions.AuthenticationException;
import com.example.cbumanage.authentication.exceptions.InvalidEmailException;
import com.example.cbumanage.authentication.exceptions.InvalidJwtException;
import com.example.cbumanage.exception.MemberException;
import com.example.cbumanage.exception.MemberNotExistsException;
import com.example.cbumanage.exception.handler.RestControllerHandlerAdvice;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.example.cbumanage.authentication.controller")
public class LoginControllerAdvice {
	private final RestControllerHandlerAdvice headHandler;

	public LoginControllerAdvice(RestControllerHandlerAdvice restControllerHandlerAdvice) {
		this.headHandler = restControllerHandlerAdvice;
	}

	@ExceptionHandler(AuthenticationException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ExceptionMessage memberNotExistsException(MemberNotExistsException e) {
		return headHandler.runtimeException(e, HttpStatus.UNAUTHORIZED.value());
	}

	@ExceptionHandler(InvalidJwtException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ExceptionMessage invalidJwtException(InvalidJwtException e) {
		return headHandler.runtimeException(e, HttpStatus.UNAUTHORIZED.value());
	}

	@ExceptionHandler(MemberException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ExceptionMessage memberException(MemberException e) {
		return headHandler.runtimeException(e, HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler(InvalidEmailException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ExceptionMessage invalidEmailException(InvalidEmailException e) {
		return headHandler.runtimeException(e, HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ExceptionMessage constraintViolationException(ConstraintViolationException e) {
		return headHandler.runtimeException(e, HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler(TypeMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ExceptionMessage typeMismatchException(TypeMismatchException e) {
		return headHandler.runtimeException(e, HttpStatus.BAD_REQUEST.value());
	}
}
