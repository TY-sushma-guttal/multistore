package com.mrmrscart.userservice.response.supplier;


import com.mrmrscart.userservice.entity.supplier.SupplierRegistration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierRegistartionGetResponse {
	private boolean error;
	private String message;
	private SupplierRegistration data;
}
