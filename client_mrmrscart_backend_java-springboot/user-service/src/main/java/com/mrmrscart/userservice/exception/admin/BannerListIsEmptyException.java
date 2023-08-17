package com.mrmrscart.userservice.exception.admin;

@SuppressWarnings("serial")
public class BannerListIsEmptyException extends RuntimeException{
	public BannerListIsEmptyException(String message) {
		super(message);
	}
}
