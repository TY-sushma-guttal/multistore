package com.mrmrscart.orderspaymentsservice.feign.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierOrdersTotalCount {

	private long totalOrders;
	private long ordersCompleted;
	private long ordersCancelled;
	private long ordersPending;
}
