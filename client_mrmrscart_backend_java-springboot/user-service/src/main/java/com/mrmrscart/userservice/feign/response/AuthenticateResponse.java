package com.mrmrscart.userservice.feign.response;

import com.mrmrscart.userservice.entity.supplier.StaffManagementInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticateResponse {
	private boolean error;
	private String message;
	private String token;
	private String refreshToken;
	private StaffManagementInfo staffDetails;
}
