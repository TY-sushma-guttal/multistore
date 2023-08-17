package com.mrmrscart.authservice.pojo.user;

import com.mrmrscart.authservice.entity.user.EUserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequestPojo {

	private String userName;
	private String password;
	private EUserRole userType;
}
