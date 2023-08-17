package com.mrmrscart.userservice.pojo.supplier;

import com.mrmrscart.userservice.entity.supplier.EUserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRole {
	private int userRoleId;
	private EUserRole role;
}
