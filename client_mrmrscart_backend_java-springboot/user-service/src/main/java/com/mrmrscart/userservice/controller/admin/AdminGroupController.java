package com.mrmrscart.userservice.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.userservice.response.admin.SuccessResponse;
import com.mrmrscart.userservice.service.admin.AdminGroupService;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
public class AdminGroupController {

	@Autowired
	private AdminGroupService adminGroupService;

	@PostMapping("/admin/group")
	public ResponseEntity<SuccessResponse> createAdminGroup() {
		adminGroupService.createAdminGroup();
		return null;

	}

}
