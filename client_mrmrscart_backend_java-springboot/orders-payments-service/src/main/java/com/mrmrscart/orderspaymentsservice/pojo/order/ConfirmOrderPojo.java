package com.mrmrscart.orderspaymentsservice.pojo.order;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmOrderPojo {

	private List<String> orderIds;
	private String logisticPartnerName;
	private String logisticUrl;
	private String trackingId;
	private Long pickupAddressId;
}
