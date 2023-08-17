package com.mrmrscart.userservice.controller.admin;

import static com.mrmrscart.userservice.common.admin.AdminRegistrationConstant.ADMIN_MANAGER_ADD_FAILURE_MESSAGE;
import static com.mrmrscart.userservice.common.admin.AdminRegistrationConstant.ADMIN_MANAGER_ADD_SUCCESS_MESSAGE;
import static com.mrmrscart.userservice.common.admin.AdminRegistrationConstant.ADMIN_MANAGER_DELETE_FAILURE_MESSAGE;
import static com.mrmrscart.userservice.common.admin.AdminRegistrationConstant.ADMIN_MANAGER_DELETE_SUCCESS_MESSAGE;
import static com.mrmrscart.userservice.common.admin.AdminRegistrationConstant.ADMIN_MANAGER_DISABLE_FAILURE_MESSAGE;
import static com.mrmrscart.userservice.common.admin.AdminRegistrationConstant.ADMIN_MANAGER_DISABLE_SUCCESS_MESSAGE;
import static com.mrmrscart.userservice.common.admin.AdminRegistrationConstant.ADMIN_MANAGER_GET_FAILURE_MESSAGE;
import static com.mrmrscart.userservice.common.admin.AdminRegistrationConstant.ADMIN_MANAGER_GET_SUCCESS_MESSAGE;
import static com.mrmrscart.userservice.common.admin.AdminRegistrationConstant.ADMIN_MANAGER_UPDATE_FAILURE_MESSAGE;
import static com.mrmrscart.userservice.common.admin.AdminRegistrationConstant.ADMIN_MANAGER_UPDATE_SUCCESS_MESSAGE;

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
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.userservice.entity.admin.AdminRegistration;
import com.mrmrscart.userservice.pojo.admin.AdminManagerFilterPojo;
import com.mrmrscart.userservice.pojo.admin.AdminManagerPojo;
import com.mrmrscart.userservice.response.admin.SuccessResponse;
import com.mrmrscart.userservice.service.admin.AdminManagerService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/users")
public class AdminManagerController {

	@Autowired
	private AdminManagerService adminManagerService;

	@Operation(summary = "This api will add the admin manager data")
	@PostMapping("/admin/admin-manager")
	public ResponseEntity<SuccessResponse> addAdminManager(@RequestBody AdminManagerPojo data) {
		AdminRegistration addAdminManager = adminManagerService.addAdminManager(data);
		if (addAdminManager != null) {
			return new ResponseEntity<>(new SuccessResponse(false, ADMIN_MANAGER_ADD_SUCCESS_MESSAGE, addAdminManager),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(new SuccessResponse(true, ADMIN_MANAGER_ADD_FAILURE_MESSAGE, addAdminManager),
				HttpStatus.OK);
	}

	@Operation(summary = "This api will fetch the admin manager data based on the id")
	@GetMapping("/admin/admin-manager/{id}")
	public ResponseEntity<SuccessResponse> getAdminManager(@PathVariable String id) {
		AdminRegistration adminManager = adminManagerService.getAdminManager(id);
		if (adminManager != null) {
			return new ResponseEntity<>(new SuccessResponse(false, ADMIN_MANAGER_GET_SUCCESS_MESSAGE, adminManager),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(new SuccessResponse(true, ADMIN_MANAGER_GET_FAILURE_MESSAGE, adminManager),
				HttpStatus.OK);

	}

	@Operation(summary = "This api will update the admin manager details")
	@PutMapping("/admin/admin-manager")
	public ResponseEntity<SuccessResponse> updateAdminManager(@RequestBody AdminManagerPojo data) {
		AdminRegistration updateAdminManager = adminManagerService.updateAdminManager(data);
		if (updateAdminManager != null) {
			return new ResponseEntity<>(
					new SuccessResponse(false, ADMIN_MANAGER_UPDATE_SUCCESS_MESSAGE, updateAdminManager),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(new SuccessResponse(true, ADMIN_MANAGER_UPDATE_FAILURE_MESSAGE, updateAdminManager),
				HttpStatus.OK);
	}

	@Operation(summary = "This api will delete the admin manager details")
	@DeleteMapping("/admin/admin-manager/{id}")
	public ResponseEntity<SuccessResponse> deleteAdminManager(@PathVariable String id) {
		boolean deleteManager = adminManagerService.deleteAdminManager(id);
		if (deleteManager) {
			return new ResponseEntity<>(new SuccessResponse(false, ADMIN_MANAGER_DELETE_SUCCESS_MESSAGE, deleteManager),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(new SuccessResponse(true, ADMIN_MANAGER_DELETE_FAILURE_MESSAGE, deleteManager),
				HttpStatus.OK);
	}

	@Operation(summary = "This api will disable the admin manager details")
	@PutMapping("/admin/admin-manager/{id}")
	public ResponseEntity<SuccessResponse> disableAdminManager(@PathVariable String id) {
		boolean disableManager = adminManagerService.disableAdminManager(id);
		if (disableManager) {
			return new ResponseEntity<>(
					new SuccessResponse(false, ADMIN_MANAGER_DISABLE_SUCCESS_MESSAGE, disableManager), HttpStatus.OK);
		}
		return new ResponseEntity<>(new SuccessResponse(true, ADMIN_MANAGER_DISABLE_FAILURE_MESSAGE, disableManager),
				HttpStatus.OK);
	}

//	@Operation(summary = "This api will fetch all the data based on date")
//	@PutMapping("/admin/admin-manager")
//	public ResponseEntity<SuccessResponse> getAllAdminManagerByFilter(AdminManagerFilterPojo data){
//		return new ResponseEntity<>(new SuccessResponse(false, ADMIN_MANAGER_UPDATE_SUCCESS_MESSAGE, ),HttpStatus.OK);
//	}
}
