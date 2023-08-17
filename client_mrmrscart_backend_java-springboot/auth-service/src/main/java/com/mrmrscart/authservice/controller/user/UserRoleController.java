package com.mrmrscart.authservice.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.authservice.pojo.user.UserRolePojo;
import com.mrmrscart.authservice.response.user.SuccessResponse;
import com.mrmrscart.authservice.service.user.UserRoleService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/auth")
public class UserRoleController {

	@Autowired
	private UserRoleService userRoleService;

	@PostMapping("/user-role")
	public ResponseEntity<SuccessResponse> addUserRole(@RequestBody UserRolePojo data) {
		return new ResponseEntity<>(
				new SuccessResponse(false, "User role added successfully. ", userRoleService.addUserRole(data)),
				HttpStatus.OK);
	}
}
