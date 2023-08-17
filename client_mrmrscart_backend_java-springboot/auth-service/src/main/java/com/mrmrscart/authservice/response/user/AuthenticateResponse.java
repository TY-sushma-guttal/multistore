package com.mrmrscart.authservice.response.user;

import com.mrmrscart.authservice.feign.pojo.StaffManagementInfo;
import com.mrmrscart.authservice.pojo.user.AdminRegistrationPojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticateResponse {

	private boolean error;
	private String message;
	private String token;
	private String refreshToken;
	private StaffManagementInfo staffDetails;
	private AdminRegistrationPojo adminRegistrationPojo;
}
