package com.mrmrscart.orderspaymentsservice.pojo.order;

import com.mrmrscart.orderspaymentsservice.enums.EModeOfOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderedProductInfoPojo {

	private String productId;
	private String orderedStoreProductId;
	private double productPrice;
	private int quantity;
	private EModeOfOrder modeOfOrder;
	private boolean isForwardOrder;
	private boolean isReturnOrder;
	// fast or normal
	private String deliveryType;
}
