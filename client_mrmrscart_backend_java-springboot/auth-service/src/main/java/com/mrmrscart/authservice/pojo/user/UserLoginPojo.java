package com.mrmrscart.authservice.pojo.user;

import java.util.List;


import com.mrmrscart.authservice.entity.user.UserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginPojo {
	private int userLoginId;
	private String emailId;
	private String password;
	private String refreshToken;
	private List<UserRole> userRoles;
}
