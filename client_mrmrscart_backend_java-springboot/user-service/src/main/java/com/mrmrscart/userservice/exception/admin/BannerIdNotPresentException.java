package com.mrmrscart.userservice.exception.admin;

@SuppressWarnings("serial")
public class BannerIdNotPresentException extends RuntimeException {
	public BannerIdNotPresentException(String message) {
		super(message);
	}
}
