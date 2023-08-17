package com.mrmrscart.userservice.feign.response;

import com.mrmrscart.userservice.entity.admin.AdminRegistration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRegistrationResponse {
	private boolean error;
	private String message;
	private AdminRegistration data;
}
