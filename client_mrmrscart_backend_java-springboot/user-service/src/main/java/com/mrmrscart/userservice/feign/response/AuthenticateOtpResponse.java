package com.mrmrscart.userservice.feign.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticateOtpResponse {
	private boolean error;
	private String message;
	private String token;
	private String refreshToken;
	private boolean isStaff;
}
