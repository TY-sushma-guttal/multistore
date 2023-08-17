package com.mrmrscart.userservice.feign.response;

import com.mrmrscart.userservice.entity.supplier.SupplierStoreInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierStoreInfoResponse {

	private boolean error;
	private String message;
	private SupplierStoreInfo data;
}
