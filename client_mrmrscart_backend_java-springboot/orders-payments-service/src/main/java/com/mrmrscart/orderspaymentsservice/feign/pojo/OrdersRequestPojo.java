package com.mrmrscart.orderspaymentsservice.feign.pojo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersRequestPojo {

	private List<String> productIds;
	private String commissionType;
}
