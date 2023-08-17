package com.mrmrscart.authservice.response.user;

import com.mrmrscart.authservice.pojo.user.AdminRegistrationPojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRegistrationResponse {

	private boolean error;
	private String message;
	private AdminRegistrationPojo data;
}
