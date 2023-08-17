package com.mrmrscart.userservice.response.supplier;


import com.mrmrscart.userservice.pojo.supplier.UserLoginPojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponse {
	
	private boolean error;
	private String message;
	private UserLoginPojo data;
}
