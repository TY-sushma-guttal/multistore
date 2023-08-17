package com.mrmrscart.authservice.response.user;

import com.mrmrscart.authservice.entity.user.UserLogin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponse {
	private boolean error;
	private String message;
	private UserLogin data;
}
