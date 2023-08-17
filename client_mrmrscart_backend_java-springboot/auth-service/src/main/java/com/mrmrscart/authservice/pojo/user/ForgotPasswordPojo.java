package com.mrmrscart.authservice.pojo.user;

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

}
