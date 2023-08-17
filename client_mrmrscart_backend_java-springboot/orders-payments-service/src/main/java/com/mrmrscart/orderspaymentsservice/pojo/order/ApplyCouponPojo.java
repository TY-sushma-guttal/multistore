package com.mrmrscart.orderspaymentsservice.pojo.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplyCouponPojo {

	private String couponCode;
	private Long couponId;

}
