package com.mrmrscart.userservice.exception.supplier;

@SuppressWarnings("serial")
public class StaffListEmptyException extends RuntimeException{
	public StaffListEmptyException(String message) {
		super(message);
	}
}
