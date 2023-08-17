package com.mrmrscart.userservice.pojo.supplier;

import com.mrmrscart.userservice.entity.supplier.EUserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordPojo {
	private String userName;
	private String newPassword;
	private String reEnterPassword;
	private EUserRole userType;

}
