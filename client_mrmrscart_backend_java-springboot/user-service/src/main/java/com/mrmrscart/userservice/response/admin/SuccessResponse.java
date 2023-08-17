package com.mrmrscart.userservice.response.admin;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuccessResponse {
	private boolean error;
	private String message;
	private Object data;
}
