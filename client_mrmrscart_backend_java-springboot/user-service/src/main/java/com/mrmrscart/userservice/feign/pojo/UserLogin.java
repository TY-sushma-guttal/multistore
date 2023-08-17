package com.mrmrscart.userservice.feign.pojo;

import java.util.List;


import com.mrmrscart.userservice.pojo.supplier.UserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLogin {
	private int userLoginId;
	private String emailId;
	private String password;
	private String refreshToken;
	private List<UserRole> userRoles;
}
