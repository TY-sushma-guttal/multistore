package com.mrmrscart.helpandsupportservice.controller;

import static com.mrmrscart.helpandsupportservice.common.HelpSupportConstant.*;

import java.util.List;

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

import com.mrmrscart.helpandsupportservice.entity.EUserType;
import com.mrmrscart.helpandsupportservice.pojo.AdminHelpSupportFilterPojo;
import com.mrmrscart.helpandsupportservice.pojo.SupplierQueryPojo;
import com.mrmrscart.helpandsupportservice.response.SuccessResponse;
import com.mrmrscart.helpandsupportservice.service.AdminHelpSupportService;
import com.mrmrscart.helpandsupportservice.wrapper.HelpSupportQueryWrapper;

import io.swagger.v3.oas.annotations.Operation;

/**
 * 
 * @author Sudharshan B S
 *
 */
@RestController
@RequestMapping("/api/v1/help-and-support")
@CrossOrigin(origins = "*")
public class AdminHelpSupportController {

	@Autowired
	private AdminHelpSupportService adminHelpSupportService;

	@Operation(summary = "This API is used for deleting the ticket by admin")
	@DeleteMapping("/admin/ticket")
	public ResponseEntity<SuccessResponse> deleteTicket(@RequestParam("ticketId") Long ticketId) {
		return new ResponseEntity<>(
				new SuccessResponse(adminHelpSupportService.deleteTicket(ticketId), DELETE_TICKET_SUCCESS, null),
				HttpStatus.OK);
	}

	@Operation(summary = "This API is used for closing the ticket by admin")
	@PutMapping("/admin/ticket")
	public ResponseEntity<SuccessResponse> closeTicket(@RequestParam("ticketId") Long ticketId) {
		return new ResponseEntity<>(
				new SuccessResponse(adminHelpSupportService.closeTicket(ticketId), CLOSE_TICKET_SUCCESS, null),
				HttpStatus.OK);
	}

	@Operation(summary = "This API is used for fetching all the tickets in admin based on the usertype with filter")
	@PostMapping("/admin/user-ticket/{pageNumber}/{pageSize}")
	public ResponseEntity<SuccessResponse> getTicketsByUserType(@RequestBody AdminHelpSupportFilterPojo filterPojo,
			@PathVariable("pageNumber") int pageNumber, @PathVariable("pageSize") int pageSize) {
		return new ResponseEntity<>(new SuccessResponse(false, GET_TICKET_SUCCESS_MESSAGE,
				adminHelpSupportService.getTicketsByUserType(filterPojo, pageNumber, pageSize)), HttpStatus.OK);
	}

	@Operation(summary = "This API is used for fetching all the filter details for the help and support")
	@GetMapping("/admin/ticket-filter")
	public ResponseEntity<SuccessResponse> getFilterInfo(@RequestParam("userType") EUserType userType) {
		return new ResponseEntity<>(
				new SuccessResponse(false, GET_TICKET_SUCCESS_MESSAGE, adminHelpSupportService.getFilterInfo(userType)),
				HttpStatus.OK);
	}

	@PostMapping("/admin/supplier/query/{pageNumber}/{pageSize}")
	public ResponseEntity<SuccessResponse> getSupplierQueries(@RequestBody SupplierQueryPojo pojo,
			@PathVariable int pageNumber, @PathVariable int pageSize) {
		List<HelpSupportQueryWrapper> supplierQueries = adminHelpSupportService.getSupplierQueries(pojo, pageNumber,
				pageSize);
		if (!supplierQueries.isEmpty()) {
			return new ResponseEntity<>(new SuccessResponse(false, GET_TICKET_SUCCESS_MESSAGE, supplierQueries),
					HttpStatus.OK);
		} else
			return new ResponseEntity<>(new SuccessResponse(true, GET_TICKET_FAILURE_MESSAGE, supplierQueries),
					HttpStatus.OK);
	}
}
