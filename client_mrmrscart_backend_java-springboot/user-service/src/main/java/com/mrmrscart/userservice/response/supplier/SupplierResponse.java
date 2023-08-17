package com.mrmrscart.userservice.response.supplier;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SupplierResponse {
	
	private boolean error;
	private String message;
	private Object data;
	
}
