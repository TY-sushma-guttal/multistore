package com.mrmrscart.userservice.feign.pojo;

import com.mrmrscart.userservice.entity.supplier.EUserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationOtpPojo {

	private String userName;
	private EUserRole userType;
	private String userId;
}
