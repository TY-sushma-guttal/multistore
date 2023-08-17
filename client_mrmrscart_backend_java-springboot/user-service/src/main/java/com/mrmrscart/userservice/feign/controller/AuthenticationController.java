package com.mrmrscart.userservice.feign.controller;

import static com.mrmrscart.userservice.common.admin.AdminConstant.GET_SUCCESS_MESSAGE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.userservice.feign.pojo.AuthenticationPojo;
import com.mrmrscart.userservice.feign.pojo.JwtInfoPojo;
import com.mrmrscart.userservice.feign.response.AdminRegistrationResponse;
import com.mrmrscart.userservice.feign.response.EmailResponse;
import com.mrmrscart.userservice.feign.response.StaffManagementInfoResponse;
import com.mrmrscart.userservice.feign.service.AuthenticationService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/users/")
public class AuthenticationController {

	@Autowired
	private AuthenticationService authenticationService;

	@PostMapping("/verify-user")
	public ResponseEntity<EmailResponse> verifyUser(@RequestBody AuthenticationPojo authenticationPojo) {
		JwtInfoPojo verifyUser = authenticationService.verifyUser(authenticationPojo);

		return new ResponseEntity<>(
				new EmailResponse(false, GET_SUCCESS_MESSAGE, verifyUser.getEmailId(), verifyUser.getUserId()),
				HttpStatus.OK);
	}

	@GetMapping("/staff/{emailId}")
	public ResponseEntity<StaffManagementInfoResponse> getStaffManagementInfo(@PathVariable String emailId) {
		return new ResponseEntity<>(new StaffManagementInfoResponse(false, GET_SUCCESS_MESSAGE,
				authenticationService.getStaffManagementInfo(emailId)), HttpStatus.OK);
	}

	@GetMapping("/admin/verify-admin")
	public ResponseEntity<AdminRegistrationResponse> getAdminInfo(@RequestParam("emailId") String emailId) {
		return new ResponseEntity<>(
				new AdminRegistrationResponse(false, GET_SUCCESS_MESSAGE, authenticationService.getAdminInfo(emailId)),
				HttpStatus.OK);
	}

}
