package com.mrmrscart.authservice.pojo.user;


import com.mrmrscart.authservice.entity.user.EUserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRolePojo {
	private String emailId;
	private EUserRole role;
}
