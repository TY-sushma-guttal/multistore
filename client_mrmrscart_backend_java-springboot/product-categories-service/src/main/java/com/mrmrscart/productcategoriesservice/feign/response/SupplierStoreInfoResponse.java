package com.mrmrscart.productcategoriesservice.feign.response;


import com.mrmrscart.productcategoriesservice.feign.pojo.SupplierStoreInfo;

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
