package com.mrmrscart.notificationreportlogservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.notificationreportlogservice.common.SupplierChangesHistoryConstants;
import com.mrmrscart.notificationreportlogservice.feign.pojo.SupplierChangesHistoryPojo;
import com.mrmrscart.notificationreportlogservice.pojo.SupplierChangesHistoryViewPojo;
import com.mrmrscart.notificationreportlogservice.response.SuccessResponse;
import com.mrmrscart.notificationreportlogservice.service.SupplierChangesHistoryService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/notification")
public class SupplierChangesHistoryController {

	@Autowired
	private SupplierChangesHistoryService supplierChangesHistoryService;

	@Operation(summary = "This method is add new supplier profile updates for approval")
	@PostMapping("/supplier-changes-history")
	public ResponseEntity<SuccessResponse> addSupplierChangesHistory(
			@RequestBody List<SupplierChangesHistoryPojo> supplierChangesHistorypojo) {
		return new ResponseEntity<>(
				new SuccessResponse(false, SupplierChangesHistoryConstants.SAVE_SUCCESS,
						supplierChangesHistoryService.addSupplierChangesHistory(supplierChangesHistorypojo)),
				HttpStatus.OK);
	}

	@Operation(summary = "This method is reject new supplier profile updates")
	@PutMapping("/supplier-changes-history/{changeId}")
	public ResponseEntity<SuccessResponse> rejectSupplierChangesHistory(@PathVariable Long changeId) {
		return new ResponseEntity<>(new SuccessResponse(false, SupplierChangesHistoryConstants.REJECT_SUCCESS,
				supplierChangesHistoryService.rejectSupplierChangesHistory(changeId)), HttpStatus.OK);
	}

	@Operation(summary = "This method is approve new supplier profile updates")
	@PutMapping("/supplier-changes-history-approved/{changeId}")
	public ResponseEntity<SuccessResponse> approveSupplierChangesHistory(@PathVariable Long changeId) {
		return new ResponseEntity<>(new SuccessResponse(false, SupplierChangesHistoryConstants.APPROVE_SUCCESS,
				supplierChangesHistoryService.approveSupplierChangesHistory(changeId)), HttpStatus.OK);
	}

	@Operation(summary = "This method is used to get all the pending supplier's profile updates")
	@GetMapping("/supplier-changes-history")
	public ResponseEntity<SuccessResponse> getAllSupplierChangesHistory() {
		List<SupplierChangesHistoryViewPojo> history = supplierChangesHistoryService.getAllSupplierChangesHistory();
		if (!history.isEmpty())
			return new ResponseEntity<>(
					new SuccessResponse(false, SupplierChangesHistoryConstants.GET_SUCCESS, history), HttpStatus.OK);
		else
			return new ResponseEntity<>(
					new SuccessResponse(false, SupplierChangesHistoryConstants.GET_FAILURE, history),
					HttpStatus.NOT_FOUND);
	}

}
