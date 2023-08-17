package com.mrmrscart.authservice.feign.pojo;

import com.mrmrscart.authservice.entity.user.EUserRole;

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
