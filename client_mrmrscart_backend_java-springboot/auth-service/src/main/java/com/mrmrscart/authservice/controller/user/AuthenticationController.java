package com.mrmrscart.authservice.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.authservice.feign.pojo.AuthenticationOtpPojo;
import com.mrmrscart.authservice.pojo.user.UserLoginRequestPojo;
import com.mrmrscart.authservice.response.user.AuthenticateOtpResponse;
import com.mrmrscart.authservice.response.user.AuthenticateResponse;
import com.mrmrscart.authservice.service.user.AuthenticationService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

	@Autowired
	private AuthenticationService authenticationService;

	@Operation(summary = "This API is used for Login")
	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticateResponse> createAuthenticationToken(
			@RequestBody UserLoginRequestPojo userLoginRequestPojo) {
		AuthenticateResponse createAuthenticationToken = authenticationService
				.createAuthenticationToken(userLoginRequestPojo);
		return new ResponseEntity<>(createAuthenticationToken, HttpStatus.OK);
	}

	@PostMapping("/otp-authenticate")
	public ResponseEntity<AuthenticateOtpResponse> createOtpAuthenticationToken(
			@RequestBody AuthenticationOtpPojo authenticationOtpPojo) {
		AuthenticateOtpResponse createAuthenticationToken = authenticationService.createOtpAuthenticationToken(authenticationOtpPojo);
		return new ResponseEntity<>(createAuthenticationToken, HttpStatus.OK);
	}

}
