package com.mrmrscart.orderspaymentsservice.pojo.order;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SerialNoUpdatePojo {

	private String orderId;
	private List<String> serialNos;
}
