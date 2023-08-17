package com.mrmrscart.authservice.service.user;

import com.mrmrscart.authservice.entity.user.UserLogin;
import com.mrmrscart.authservice.pojo.user.UserRolePojo;

public interface UserRoleService {
	public UserLogin addUserRole(UserRolePojo data);
}
