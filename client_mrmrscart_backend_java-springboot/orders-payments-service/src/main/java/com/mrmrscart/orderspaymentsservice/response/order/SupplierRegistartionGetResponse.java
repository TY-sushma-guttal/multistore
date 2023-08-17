package com.mrmrscart.orderspaymentsservice.response.order;

import com.mrmrscart.orderspaymentsservice.feign.pojo.SupplierRegistration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierRegistartionGetResponse {
	private boolean error;
	private String message;
	private SupplierRegistration data;
}
