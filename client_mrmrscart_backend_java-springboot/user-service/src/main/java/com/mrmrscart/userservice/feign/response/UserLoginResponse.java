package com.mrmrscart.userservice.feign.response;


import com.mrmrscart.userservice.feign.pojo.UserLogin;

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
