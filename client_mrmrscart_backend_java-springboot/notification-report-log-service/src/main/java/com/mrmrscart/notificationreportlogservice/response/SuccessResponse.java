package com.mrmrscart.notificationreportlogservice.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuccessResponse {
	
	private boolean error;
	private String message;
	private Object data;

}
