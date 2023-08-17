package com.mrmrscart.userservice.pojo.admin;

import com.mrmrscart.userservice.entity.supplier.EUserRole;

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
