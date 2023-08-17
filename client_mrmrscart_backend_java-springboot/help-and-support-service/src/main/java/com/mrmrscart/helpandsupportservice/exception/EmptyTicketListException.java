package com.mrmrscart.helpandsupportservice.exception;

@SuppressWarnings("serial")
public class EmptyTicketListException extends RuntimeException{
	public EmptyTicketListException(String message) {
		super(message);
	}
}
