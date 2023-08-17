package com.mrmrscart.helpandsupportservice.exception;

@SuppressWarnings("serial")
public class TicketNotFoundException extends RuntimeException {
	public TicketNotFoundException(String message) {
		super(message);
	}

}
