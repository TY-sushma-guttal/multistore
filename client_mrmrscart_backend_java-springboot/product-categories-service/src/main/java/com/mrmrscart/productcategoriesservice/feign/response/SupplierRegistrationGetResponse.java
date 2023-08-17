package com.mrmrscart.productcategoriesservice.feign.response;

import com.mrmrscart.productcategoriesservice.feign.pojo.SupplierRegistration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierRegistrationGetResponse {

	private boolean error;
	private String message;
	private SupplierRegistration data;

}
