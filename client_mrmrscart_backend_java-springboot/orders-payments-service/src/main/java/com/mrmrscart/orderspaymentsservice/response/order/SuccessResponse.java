package com.mrmrscart.orderspaymentsservice.response.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuccessResponse {

	private boolean error;
	private String message;
	private Object data;
}
