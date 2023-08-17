package com.mrmrscart.userservice.pojo.supplier;

import com.mrmrscart.userservice.entity.supplier.EUserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordPojo {

	private String emailId;
	private String oldPassword;
	private String newPassword;
	private String reEnterPassword;
	private EUserRole userType;
}
