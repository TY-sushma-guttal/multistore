package com.mrmrscart.userservice.wrapper.customer;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerWrapper {
	private String customerId;
	private String profileImageUrl;
	private String customerType;
	private String customerName;
	private String mobileNumber;
	private BigDecimal lastMonthPurchase;
	private BigDecimal currentMonthPurchase;
	private BigDecimal totalPurchase;

}
