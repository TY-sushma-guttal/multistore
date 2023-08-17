package com.mrmrscart.notificationreportlogservice.response;

import com.mrmrscart.notificationreportlogservice.feign.pojo.SupplierRegistrationPojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierResponse {
	
	private boolean error;
	private String message;
	private SupplierRegistrationPojo data;

}
