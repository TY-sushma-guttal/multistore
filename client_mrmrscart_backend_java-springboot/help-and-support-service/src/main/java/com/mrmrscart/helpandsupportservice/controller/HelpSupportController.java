package com.mrmrscart.helpandsupportservice.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.helpandsupportservice.common.HelpSupportConstant;
import com.mrmrscart.helpandsupportservice.entity.EUserType;
import com.mrmrscart.helpandsupportservice.entity.HelpSupport;
import com.mrmrscart.helpandsupportservice.pojo.HelpSupportFilterPojo;
import com.mrmrscart.helpandsupportservice.pojo.HelpSupportMessagePojo;
import com.mrmrscart.helpandsupportservice.pojo.HelpSupportMessageViewPojo;
import com.mrmrscart.helpandsupportservice.pojo.HelpSupportPojo;
import com.mrmrscart.helpandsupportservice.response.SuccessResponse;
import com.mrmrscart.helpandsupportservice.service.HelpSupportService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/help-and-support")
public class HelpSupportController {

	@Autowired
	private HelpSupportService helpSupportService;

	@Operation(summary = "This method is used to create tickets")
	@PostMapping("/ticket")
	public ResponseEntity<SuccessResponse> createTicket(@RequestBody HelpSupportPojo helpSupportPojo) {
		return new ResponseEntity<>(new SuccessResponse(false, HelpSupportConstant.CREATE_TICKET_SUCCESS_MESSAGE,
				helpSupportService.createTicket(helpSupportPojo)), HttpStatus.OK);
	}

	@Operation(summary = "This method is used to get all the tickets")
	@PostMapping("/supplier/ticket/{pageNumber}/{pageSize}")
	public ResponseEntity<SuccessResponse> getAllSupplierTickets(@PathVariable int pageNumber,
			@PathVariable int pageSize, @RequestBody HelpSupportFilterPojo pojo) {
		List<HelpSupport> tickets = helpSupportService.getAllSupplierTickets(pageNumber, pageSize, pojo);
		if (!tickets.isEmpty()) {
			return new ResponseEntity<>(
					new SuccessResponse(false, HelpSupportConstant.GET_TICKET_SUCCESS_MESSAGE, tickets), HttpStatus.OK);

		} else {
			return new ResponseEntity<>(
					new SuccessResponse(true, HelpSupportConstant.GET_TICKET_FAILURE_MESSAGE, tickets), HttpStatus.OK);

		}
	}

	@Operation(summary = "This method is used to view the messages of a ticket")
	@PutMapping("/ticket")
	public ResponseEntity<SuccessResponse> viewTicket(@RequestBody HelpSupportMessageViewPojo messageViewPojo) {
		return new ResponseEntity<>(new SuccessResponse(false, HelpSupportConstant.VIEW_TICKET_SUCCESS_MESSAGE,
				helpSupportService.viewTicket(messageViewPojo)), HttpStatus.OK);
	}

	@Operation(summary = "This method is used to send reply to any ticket")
	@PutMapping("/ticket-reply")
	public ResponseEntity<SuccessResponse> sendReply(@RequestBody HelpSupportMessagePojo helpSupportMessagePojo) {
		return new ResponseEntity<>(new SuccessResponse(false, HelpSupportConstant.REPLY_TICKET_SUCCESS_MESSAGE,
				helpSupportService.sendReply(helpSupportMessagePojo)), HttpStatus.OK);
	}

	/**
	 * @author Soumyajit
	 * @param userFromType
	 * @return
	 */
	@Operation(summary = "This api will give ticket count based on user type for dashboard")
	@GetMapping("/ticket/dashboard")
	public ResponseEntity<SuccessResponse> getDashboardData() {
		return new ResponseEntity<>(new SuccessResponse(false, "Help Support Dashboard Data Fetched Successfully",
				helpSupportService.getDashboardData()), HttpStatus.OK);
	}

	
	/**
	 * @author Soumyajit
	 * @param userFromType
	 * @param ticketStatus
	 * @return
	 */
	@GetMapping("/ticket/dashboard/type")
	public ResponseEntity<SuccessResponse> getIssueTypeCountByStatusAndUserType(@RequestParam EUserType userFromType) {
		return new ResponseEntity<>(
				new SuccessResponse(false, "Help Support Dashboard Data Fetched Successfully",
						helpSupportService.getTicketCountByIssueTypeAndStatus(userFromType)),
				HttpStatus.OK);
	}
}
