package com.mrmrscart.orderspaymentsservice.feign.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerStores {
	private long customerStoreId;

	private EUserRole storeType;

	private String storeCode;

	private boolean isFavourite;

	private CustomerRegistration customerRegistration;
}
