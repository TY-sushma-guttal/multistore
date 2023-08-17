package com.mrmrscart.orderspaymentsservice.pojo.order;

import java.util.List;

import com.mrmrscart.orderspaymentsservice.entity.order.EUserType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfoPojo {

	private double totalOrderAmount;
	//
	private EUserType productOwnerType;
	// supplierId
	private String productOwnerId;
	// customerId
	private String orderedById;
	//
	private EUserType orderedStoreType;

	// customer address
	private Long billingAddressId;
	// customer address
	private Long shippingAddressId;

	// customer profile id
	private String profileId;

	private List<OrderedProductInfoPojo> productInfos;
	private ApplyCouponPojo couponPojo;
}
