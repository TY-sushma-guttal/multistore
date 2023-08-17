package com.mrmrscart.userservice.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.mrmrscart.userservice.feign.pojo.AuthenticationOtpPojo;
import com.mrmrscart.userservice.feign.pojo.CustomerPojo;
import com.mrmrscart.userservice.feign.response.AuthenticateOtpResponse;
import com.mrmrscart.userservice.pojo.admin.UserRolePojo;
import com.mrmrscart.userservice.pojo.supplier.ChangePasswordPojo;
import com.mrmrscart.userservice.pojo.supplier.ForgotPasswordPojo;
import com.mrmrscart.userservice.pojo.supplier.UserLoginPojo;
import com.mrmrscart.userservice.response.supplier.SuccessResponse;
import com.mrmrscart.userservice.response.supplier.UserLoginResponse;

@FeignClient(name = "auth-service")
public interface AuthServiceClient {

	@GetMapping("/api/v1/auth/user-email/{emailId}")
	public ResponseEntity<UserLoginResponse> getUserByEmailId(@PathVariable String emailId);

	@PostMapping("/api/v1/auth/user-email/{emailId}")
	public ResponseEntity<UserLoginResponse> addUserEmail(@PathVariable String emailId);
	
	@PutMapping("/api/v1/auth/user/reset-password")
	public ResponseEntity<UserLoginResponse> resetPassword(@RequestBody ForgotPasswordPojo forgotPasswordPojo);

	@PutMapping("/api/v1/auth/user/change-password")
	public ResponseEntity<UserLoginResponse> changePassword(@RequestBody ChangePasswordPojo changePasswordPojo);
	
	@PostMapping("/api/v1/auth/user-role")
	public ResponseEntity<SuccessResponse> addUserRole(@RequestBody UserRolePojo data);
	
	@PostMapping("/api/v1/auth/user")
	public ResponseEntity<SuccessResponse> addUser(@RequestBody UserLoginPojo data); 

	@PostMapping("/api/v1/auth/otp-authenticate")
	public ResponseEntity<AuthenticateOtpResponse> createOtpAuthenticationToken(
			@RequestBody AuthenticationOtpPojo authenticationPojo);
	
	@PostMapping("/api/v1/auth/user/customer-registration")
	public ResponseEntity<SuccessResponse> addCustomer(@RequestBody CustomerPojo customerPojo) ;
}
