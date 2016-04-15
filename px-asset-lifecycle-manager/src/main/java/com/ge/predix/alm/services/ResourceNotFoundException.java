package com.ge.predix.alm.services;

public class ResourceNotFoundException extends Exception{

	private static final long serialVersionUID = 2802148573614522931L;
	
	public ResourceNotFoundException(String message) {
		super(message);
	}


	public ResourceNotFoundException() {
		super("Resource Not Found");
	}
}