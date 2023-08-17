package com.mrmrscart.userservice.pojo.supplier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevenueSalesTotalCountPojo {

	private long totalOrders;
	private long totalSalesValue;
	private long totalCustomers;
	private long totalFreeOrders;
	private long totalReferrals;
}
