package com.mrmrscart.userservice.pojo.supplier;

import java.util.List;


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
