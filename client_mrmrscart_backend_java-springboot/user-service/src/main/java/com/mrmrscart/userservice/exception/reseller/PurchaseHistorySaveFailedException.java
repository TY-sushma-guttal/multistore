package com.mrmrscart.userservice.exception.reseller;

@SuppressWarnings("serial")
public class PurchaseHistorySaveFailedException extends RuntimeException {
	public PurchaseHistorySaveFailedException(String message) {
		super(message);
	}
}
