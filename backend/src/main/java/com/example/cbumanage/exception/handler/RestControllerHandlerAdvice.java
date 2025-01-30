package com.example.cbumanage.exception.handler;

import com.example.cbumanage.authentication.exceptions.handler.ExceptionMessage;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestControllerHandlerAdvice {


	@ExceptionHandler(RuntimeException.class)
	public ExceptionMessage runtimeException(RuntimeException e) {
		return new ExceptionMessage(e.getClass().getName(), e.getMessage(), 500);
	}

	public ExceptionMessage runtimeException(RuntimeException e, Integer status) {
		return new ExceptionMessage(e.getClass().getName(), e.getMessage(), status);
	}
}
