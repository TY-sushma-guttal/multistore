package com.mrmrscart.userservice.controller.admin;

import static com.mrmrscart.userservice.common.admin.AdminConstant.ADMIN_CONFIG_SUCCESS;
import static com.mrmrscart.userservice.common.supplier.SupplierConstant.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.userservice.pojo.admin.AdminConfigurationPojo;
import com.mrmrscart.userservice.response.admin.SuccessResponse;
import com.mrmrscart.userservice.service.admin.AdminConfigurationService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/users/")
@Slf4j
public class AdminConfigurationController {

	@Autowired
	private AdminConfigurationService adminConfigurationService;

	@Operation(summary = "This Api is used to add Admin-Configuration")
	@PostMapping("/admin-configuration")
	public ResponseEntity<SuccessResponse> addAdminConfiguration(
			@RequestBody AdminConfigurationPojo adminConfigurationPojo) {
		log.debug(DEBUG_MESSAGE);
		return new ResponseEntity<>(new SuccessResponse(false, ADMIN_CONFIG_SUCCESS,
				adminConfigurationService.addAdminConfiguration(adminConfigurationPojo)), HttpStatus.OK);
	}
}
