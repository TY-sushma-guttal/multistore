package com.mrmrscart.authservice.controller.user;

import static com.mrmrscart.authservice.common.user.UserConstant.EMAIL_SUCCESS;
import static com.mrmrscart.authservice.common.user.UserConstant.USER_LOGIN_ADD_SUCCESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.authservice.pojo.user.ChangePasswordPojo;
import com.mrmrscart.authservice.pojo.user.CustomerPojo;
import com.mrmrscart.authservice.pojo.user.ForgotPasswordPojo;
import com.mrmrscart.authservice.pojo.user.UserLoginPojo;
import com.mrmrscart.authservice.response.user.SuccessResponse;
import com.mrmrscart.authservice.response.user.UserLoginResponse;
import com.mrmrscart.authservice.service.user.UserLoginService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/auth")
public class UserLoginController {

	@Autowired
	private UserLoginService userLoginService;

	@GetMapping("/user-email/{emailId}")
	public ResponseEntity<UserLoginResponse> getUserByEmailId(@PathVariable String emailId) {
		return new ResponseEntity<>(
				new UserLoginResponse(false, EMAIL_SUCCESS, userLoginService.getUserByEmailId(emailId)), HttpStatus.OK);
	}

	@PostMapping("/user-email/{emailId}")
	public ResponseEntity<UserLoginResponse> addUserEmail(@PathVariable String emailId) {
		return new ResponseEntity<>(new UserLoginResponse(false, EMAIL_SUCCESS, userLoginService.addUserEmail(emailId)),
				HttpStatus.OK);
	}

	@PutMapping("/user/reset-password")
	public ResponseEntity<UserLoginResponse> resetPassword(@RequestBody ForgotPasswordPojo forgotPasswordPojo) {
		return new ResponseEntity<>(new UserLoginResponse(false, "Password reset successful!",
				userLoginService.resetPassword(forgotPasswordPojo)), HttpStatus.OK);
	}

	@PutMapping("/user/change-password")
	public ResponseEntity<UserLoginResponse> changePassword(@RequestBody ChangePasswordPojo changePasswordPojo) {
		return new ResponseEntity<>(new UserLoginResponse(false, "Password update successful!",
				userLoginService.changePassword(changePasswordPojo)), HttpStatus.OK);
	}

	@PostMapping("/user")
	public ResponseEntity<SuccessResponse> addUser(@RequestBody UserLoginPojo data) {
		return new ResponseEntity<>(
				new SuccessResponse(false, USER_LOGIN_ADD_SUCCESS, userLoginService.addUser(data)),
				HttpStatus.OK);
	}

	/*
	 * @Author : Hemadri G
	 */
	@PostMapping("/user/customer-registration")
	public ResponseEntity<SuccessResponse> addCustomer(@RequestBody CustomerPojo customerPojo) {
		return new ResponseEntity<>(
				new SuccessResponse(false, USER_LOGIN_ADD_SUCCESS, userLoginService.addCustomer(customerPojo)), HttpStatus.OK);
	}
}
