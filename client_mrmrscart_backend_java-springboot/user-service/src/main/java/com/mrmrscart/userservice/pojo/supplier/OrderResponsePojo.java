package com.mrmrscart.userservice.pojo.supplier;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponsePojo {

	private String orderId;
	private String productName;
	private String customerName;
	private LocalDateTime orderDate;
	private double orderAmount;
	private String orderStatus;
	
	
	
	
}
