package com.mrmrscart.userservice.feign.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailResponse {
	private boolean error;
	private String message;
	private String emailId;
	private String userId;
}
