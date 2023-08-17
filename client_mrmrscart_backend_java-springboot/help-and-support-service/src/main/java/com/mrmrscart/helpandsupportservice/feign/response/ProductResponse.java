package com.mrmrscart.helpandsupportservice.feign.response;

import lombok.Data;

@Data
public class ProductResponse {
	
	private boolean error;
	private String message;
	private Object data;

}
