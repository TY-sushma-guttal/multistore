package com.mrmrscart.notificationreportlogservice.controller;

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

import com.mrmrscart.notificationreportlogservice.entity.MarketingToolNotification;
import com.mrmrscart.notificationreportlogservice.pojo.MarketingToolNotificationFilterPojo;
import com.mrmrscart.notificationreportlogservice.pojo.MarketingToolNotificationPojo;
import com.mrmrscart.notificationreportlogservice.pojo.MarketingToolNotificationViewPojo;
import com.mrmrscart.notificationreportlogservice.response.SuccessResponse;
import com.mrmrscart.notificationreportlogservice.service.MarketingToolNotificationService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/notification")
public class MarketingToolNotificationController {

	@Autowired
	private MarketingToolNotificationService marketingToolNotificationService;

	@Operation(summary = "This method is used to add marketing tool notifications")
	@PostMapping("/marketing-tool-notification")
	public ResponseEntity<SuccessResponse> addMarketingToolNotification(
			@RequestBody MarketingToolNotificationPojo marketingToolNotificationPojo) {
		return new ResponseEntity<>(
				new SuccessResponse(false, "Notification added successfully",
						marketingToolNotificationService.addMarketingToolNotification(marketingToolNotificationPojo)),
				HttpStatus.OK);
	}

	@Operation(summary = "This method is used to delete marketing tool notification")
	@DeleteMapping("/marketing-tool-notification/{marketingToolNotificationId}")
	public ResponseEntity<SuccessResponse> deleteMarketingToolNotification(
			@PathVariable Long marketingToolNotificationId) {
		return new ResponseEntity<>(
				new SuccessResponse(false, "Notification deleted successfully",
						marketingToolNotificationService.deleteMarketingToolNotification(marketingToolNotificationId)),
				HttpStatus.OK);
	}

	@Operation(summary = "This method is used to get all marketing tool notifications")
	@GetMapping("/marketing-tool-notification/{supplierId}")
	public ResponseEntity<SuccessResponse> getAllMarketingToolNotifications(@PathVariable String supplierId) {
		List<MarketingToolNotificationViewPojo> notifications = marketingToolNotificationService
				.getAllMarketingToolNotifications(supplierId);
		if (!notifications.isEmpty())
			return new ResponseEntity<>(new SuccessResponse(false, "Notifications fetched successfully", notifications),
					HttpStatus.OK);
		else
			return new ResponseEntity<>(new SuccessResponse(true, "Unable to fetch notifications", notifications),
					HttpStatus.OK);
	}

	@Operation(summary = "This method is used to get all marketing tool notifications based on search")
	@PostMapping("/marketing-tool-notification/{pageNumber}/{pageSize}/{supplierId}")
	public ResponseEntity<SuccessResponse> getPaginatedMarketingToolNotifications(@PathVariable int pageNumber,
			@PathVariable int pageSize, @PathVariable String supplierId,
			@RequestBody MarketingToolNotificationFilterPojo filterPojo) {
		List<MarketingToolNotificationViewPojo> notifications = marketingToolNotificationService
				.getPaginatedMarketingToolNotifications(pageNumber, pageSize, supplierId, filterPojo);
		if (!notifications.isEmpty())
			return new ResponseEntity<>(new SuccessResponse(false, "Notifications fetched successfully", notifications),
					HttpStatus.OK);
		else
			return new ResponseEntity<>(new SuccessResponse(true, "Unable to fetch notifications", notifications),
					HttpStatus.OK);
	}

	@Operation(summary = "This method is used to get all marketing tool notifications based on search")
	@PutMapping("/marketing-tool-notification/{marketingToolNotificationId}")
	public ResponseEntity<SuccessResponse> sendNotification(@PathVariable Long marketingToolNotificationId) {
		MarketingToolNotification notifications = marketingToolNotificationService
				.sendNotification(marketingToolNotificationId);
		if (notifications != null)
			return new ResponseEntity<>(new SuccessResponse(false, "Notifications fetched successfully", notifications),
					HttpStatus.OK);
		else
			return new ResponseEntity<>(new SuccessResponse(true, "Unable to fetch notifications", notifications),
					HttpStatus.OK);
	}

	@Operation(summary = "This method is used to get marketing tool notification by id")
	@GetMapping("/marketing-tool-notification")
	public ResponseEntity<SuccessResponse> getNotificationById(
			@RequestParam("notificationId") Long marketingToolNotificationId) {
		return new ResponseEntity<>(
				new SuccessResponse(false, "Notification fetched successfully",
						marketingToolNotificationService
								.getNotificationById(marketingToolNotificationId)),
				HttpStatus.OK);
	}

}
