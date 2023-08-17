package com.mrmrscart.userservice.feign.pojo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPojo {

	private String orderId;
	private String orderStatus;
	private double orderAmount;
	private LocalDateTime orderDate;
	private String productId;
	private String orderedById;
}
