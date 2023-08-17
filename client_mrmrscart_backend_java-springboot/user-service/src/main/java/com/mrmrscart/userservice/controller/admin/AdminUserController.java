package com.mrmrscart.userservice.controller.admin;

import static com.mrmrscart.userservice.common.admin.AdminRegistrationConstant.ADIM_USER_DISABLE_ENABLE_SUCCESS_MESSAGE;
import static com.mrmrscart.userservice.common.admin.AdminRegistrationConstant.ADMIN_USER_ADD_SUCCESS_MESSAGE;
import static com.mrmrscart.userservice.common.admin.AdminRegistrationConstant.ADMIN_USER_DELETE_SUCCESS_MESSAGE;
import static com.mrmrscart.userservice.common.admin.AdminRegistrationConstant.ADMIN_USER_UPDATE_SUCCESS_MESSAGE;

import javax.mail.SendFailedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.userservice.pojo.admin.AdminUserFilterPojo;
import com.mrmrscart.userservice.pojo.admin.AdminUserPojo;
import com.mrmrscart.userservice.response.admin.SuccessResponse;
import com.mrmrscart.userservice.service.admin.AdminUserService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/users")
public class AdminUserController {

	@Autowired
	private AdminUserService adminUserService;

	@Operation(summary = "This API is used to add new Admin User")
	@PostMapping("/admin/admin-users")
	public ResponseEntity<SuccessResponse> createAdminUser(@RequestBody AdminUserPojo adminUserPojo)
			throws SendFailedException {
		return new ResponseEntity<>(new SuccessResponse(false, ADMIN_USER_ADD_SUCCESS_MESSAGE,
				adminUserService.createAdminUser(adminUserPojo)), HttpStatus.OK);
	}

	@Operation(summary = "This API is used to update an existing Admin User")
	@PutMapping("/admin/admin-users")
	public ResponseEntity<SuccessResponse> updateAdminUser(@RequestBody AdminUserPojo adminUserPojo) {
		return new ResponseEntity<>(new SuccessResponse(false, ADMIN_USER_UPDATE_SUCCESS_MESSAGE,
				adminUserService.updateAdminUser(adminUserPojo)), HttpStatus.OK);
	}

	@Operation(summary = "This API is used to delete an existing Admin User")
	@DeleteMapping("/admin/admin-users")
	public ResponseEntity<SuccessResponse> deleteAdminUser(@RequestParam String adminRegistrationId) {
		return new ResponseEntity<>(new SuccessResponse(false, ADMIN_USER_DELETE_SUCCESS_MESSAGE,
				adminUserService.deleteAdminUser(adminRegistrationId)), HttpStatus.OK);
	}

	@Operation(summary = "This API is used to diable/enable an existing Admin User")
	@PutMapping("/admin/admin-users/{adminRegistrationId}")
	public ResponseEntity<SuccessResponse> disableAdminUser(@PathVariable String adminRegistrationId,
			@RequestParam String status) {
		return new ResponseEntity<>(new SuccessResponse(false, ADIM_USER_DISABLE_ENABLE_SUCCESS_MESSAGE,
				adminUserService.disableAdminUser(adminRegistrationId, status)), HttpStatus.OK);
	}

	@Operation(summary = "This API is used to get all Admin Users")
	@PostMapping("/admin/admin-users/{pageNumber}/{pageSize}")
	public ResponseEntity<SuccessResponse> getAllAdminUsers(@PathVariable int pageNumber, @PathVariable int pageSize,
			@RequestBody AdminUserFilterPojo adminUserFilterPojo) {
		return new ResponseEntity<>(new SuccessResponse(false, "fetched",
				adminUserService.getAllAdminUsers(pageNumber, pageSize, adminUserFilterPojo)), HttpStatus.OK);
	}

	@Operation(summary = "This API is used to get all the Admin Status")
	@GetMapping("/admin/admin-status")
	public ResponseEntity<SuccessResponse> getAllStatus() {
		return new ResponseEntity<>(new SuccessResponse(false, "Admin Status Dropdown Feched Successfully",
				adminUserService.getAllStatus()), HttpStatus.OK);
	}

	@Operation(summary = "This API is used to populate data for Created By filter dropdown")
	@GetMapping("/admin/filter")
	public ResponseEntity<SuccessResponse> getAllCreatedBy() {
		return new ResponseEntity<>(new SuccessResponse(false, "Admin Created By Dropdown Feched Successfully",
				adminUserService.getAllCreatedBy()), HttpStatus.OK);
	}

}
