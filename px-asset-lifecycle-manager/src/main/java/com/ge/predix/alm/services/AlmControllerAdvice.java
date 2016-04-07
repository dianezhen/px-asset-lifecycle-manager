package com.ge.predix.alm.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * Class used to catch exceptions thrown from the SampleController and map into HttpResponses
 * with user friendly information
 */
@ControllerAdvice
@RequestMapping(produces = "application/vnd.error")
@ResponseBody
public class AlmControllerAdvice {
	
	/**
	 * error advice nice override to throw HttpStatus NOT FOUND for 
	 * File Not Found Exceptions
	 * @param ex when file is not found
	 * @return VndErrors
	 */
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ExceptionHandler(FileNotFoundException.class)
	public VndErrors fileNotFoundException(FileNotFoundException ex) {
		return this.error(ex, ex.getLocalizedMessage());
	}
	
	
	/**
	 * error advice nice override to throw HttpStatus NOT FOUND for 
	 * File Not Found Exceptions
	 * @param ex when file is not found
	 * @return VndErrors
	 */
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ExceptionHandler(NullPointerException.class)
	public VndErrors nullPointerException(NullPointerException ex) {
		return this.error(ex, ex.getLocalizedMessage());
	}

	/**
	 * error advice nice override to throw HttpStatus 'Method Not Allowed' for 
	 * IOException
	 * @param e when io exception
	 * @return VndErrors
	 */
	@ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
	@ExceptionHandler(IOException.class)
	public VndErrors ioException(IOException e) {
		return this.error(e, e.getMessage());
	}
	
	/**
	 * error advice nice override to throw HttpStatus Bad Request for 
	 * all other exceptions
	 * @param e when generic exception
	 * @return VndErrors
	 */
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(Exception.class)
	public VndErrors exception(Exception e) {
		return this.error(e, e.getMessage());
	}

	private <E extends Exception> VndErrors error(E e, String logref) {
		String msg = Optional.of(e.getMessage()).orElse(e.getClass().getSimpleName());
		return new VndErrors(logref, msg);
	}
}
