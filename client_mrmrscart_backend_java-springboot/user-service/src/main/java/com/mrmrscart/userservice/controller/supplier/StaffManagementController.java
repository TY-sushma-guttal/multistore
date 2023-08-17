package com.mrmrscart.userservice.controller.supplier;

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

import com.mrmrscart.userservice.entity.supplier.EStaffStatus;
import com.mrmrscart.userservice.entity.supplier.StaffManagementInfo;
import com.mrmrscart.userservice.pojo.supplier.StaffManagementPojo;
import com.mrmrscart.userservice.response.supplier.SuccessResponse;
import com.mrmrscart.userservice.service.supplier.StaffManagementService;

import io.swagger.v3.oas.annotations.Operation;

import static com.mrmrscart.userservice.common.supplier.StaffManagementConstant.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/users/")
public class StaffManagementController {

	@Autowired
	private StaffManagementService staffManagementService;

	@PostMapping("/staff-management")
	public ResponseEntity<SuccessResponse> addStaffInfo(@RequestBody StaffManagementPojo data) {
		String addStaffInfo = staffManagementService.addStaffInfo(data);
		if (!addStaffInfo.isBlank() && !addStaffInfo.isEmpty()) {
			return new ResponseEntity<>(new SuccessResponse(false, ADD_STAFF_SUCCESS_MESSAGE, addStaffInfo),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new SuccessResponse(true, ADD_STAFF_FAILURE_MESSAGE, addStaffInfo),
					HttpStatus.OK);
		}
	}

	@GetMapping("/staff-management/{id}")
	public ResponseEntity<SuccessResponse> getStaffInfo(@PathVariable String id) {
		return new ResponseEntity<>(
				new SuccessResponse(false, GET_STAFF_SUCCESS_MESSAGE, staffManagementService.getStaffInfo(id)),
				HttpStatus.OK);
	}

	@DeleteMapping("/staff-management/{id}")
	public ResponseEntity<SuccessResponse> deleteStaffInfo(@PathVariable String id) {
		return new ResponseEntity<>(
				new SuccessResponse(false, DELETE_STAFF_SUCCESS_MESSAGE, staffManagementService.deleteStaffInfo(id)),
				HttpStatus.OK);
	}

	@GetMapping("/staff-management/{pageNumber}/{pageSize}")
	public ResponseEntity<SuccessResponse> getAllStaff(@PathVariable int pageNumber, @PathVariable int pageSize) {
		return new ResponseEntity<>(new SuccessResponse(false, GET_STAFF_SUCCESS_MESSAGE,
				staffManagementService.getAllStaffInfo(pageNumber, pageSize)), HttpStatus.OK);
	}

	@GetMapping("/staff-management-info/{pageNumber}/{pageSize}")
	public ResponseEntity<SuccessResponse> getAllbySupplierId(@RequestParam String supplierId,
			@PathVariable int pageNumber, @PathVariable int pageSize) {
		return new ResponseEntity<>(
				new SuccessResponse(false, GET_STAFF_SUCCESS_MESSAGE,
						staffManagementService.getAllStaffInfoBySupllierId(supplierId, pageNumber, pageSize)),
				HttpStatus.OK);
	}

	@GetMapping("/staff-management/filter/{pageNumber}/{pageSize}")
	public ResponseEntity<SuccessResponse> staffFilter(@RequestParam String supplierId, @PathVariable int pageNumber,
			@PathVariable int pageSize, @RequestParam EStaffStatus type, @RequestParam String keyword) {
		List<StaffManagementInfo> staffFilter = staffManagementService.staffFilter(supplierId, pageNumber, pageSize,
				type, keyword);
		if (staffFilter.isEmpty()) {
			return new ResponseEntity<>(new SuccessResponse(true, EMPTY_STAFF_LIST, staffFilter), HttpStatus.OK);
		}
		return new ResponseEntity<>(new SuccessResponse(false, GET_STAFF_SUCCESS_MESSAGE, staffFilter), HttpStatus.OK);
	}

	@Operation(summary = "This api will update the staff details")
	@PutMapping("/staff-management")
	public ResponseEntity<SuccessResponse> updateStaffInformation(@RequestBody StaffManagementPojo data) {
		StaffManagementInfo updateStaffInfo = staffManagementService.updateStaffInfo(data);
		if (updateStaffInfo != null) {
			return new ResponseEntity<>(new SuccessResponse(false, "Staff Data Updated Successfully", updateStaffInfo),
					HttpStatus.OK);
		}
		return new ResponseEntity<>(new SuccessResponse(true, "Staff Data Not Updated", updateStaffInfo),
				HttpStatus.OK);
	}
}
