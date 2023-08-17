package com.mrmrscart.userservice.feign.controller;

import static com.mrmrscart.userservice.common.supplier.SupplierConstant.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.userservice.feign.pojo.UserInfoWrapper;
import com.mrmrscart.userservice.feign.response.UserDetailsResponse;
import com.mrmrscart.userservice.feign.service.UserDetailsService;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
public class UserServiceController {

	@Autowired
	private UserDetailsService userDetailsService;

	@PostMapping("/user-info")
	public ResponseEntity<UserDetailsResponse> getUserDetails(@RequestBody UserInfoWrapper wrapper) {
		return new ResponseEntity<>(
				new UserDetailsResponse(false, USER_DETAILS_GET_SUCCESS, userDetailsService.getUserDetails(wrapper)),
				HttpStatus.OK);
	}

}
