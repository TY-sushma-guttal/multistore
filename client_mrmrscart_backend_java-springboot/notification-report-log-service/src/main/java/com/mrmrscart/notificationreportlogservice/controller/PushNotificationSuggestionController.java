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

import com.mrmrscart.notificationreportlogservice.entity.PushNotificationSuggestion;
import com.mrmrscart.notificationreportlogservice.pojo.PushNotificationSuggestionPojo;
import com.mrmrscart.notificationreportlogservice.response.SuccessResponse;
import com.mrmrscart.notificationreportlogservice.service.PushNotificationSuggestionService;

import io.swagger.v3.oas.annotations.Operation;

import static com.mrmrscart.notificationreportlogservice.common.PushNotificationSuggestionConstant.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/notification")
public class PushNotificationSuggestionController {

	@Autowired
	private PushNotificationSuggestionService pushNotificationSuggestionService;

	/*
	 * @Author : Hemadri G
	 */
	@Operation(summary = "This API is used to save push notification suggestions")
	@PostMapping("/push-notification-suggestion")
	public ResponseEntity<SuccessResponse> createPushNotificationSuggestion(@RequestBody List<String> titles) {
		return new ResponseEntity<>(new SuccessResponse(false, SAVE_SUCCESS,
				pushNotificationSuggestionService.createPushNotificationSuggestion(titles)), HttpStatus.OK);
	}

	/*
	 * @Author : Hemadri G
	 */
	@Operation(summary = "This API will fetch the push notification suggestions in admin screen")
	@GetMapping("/push-notification-suggestion/{pageNumber}/{pageSize}")
	public ResponseEntity<SuccessResponse> getPushNotificationSuggestion(@PathVariable("pageNumber") int pageNumber,
			@PathVariable("pageSize") int pageSize) {
		List<PushNotificationSuggestion> pushNotificationSuggestion = pushNotificationSuggestionService
				.getPushNotificationSuggestion(pageNumber, pageSize);
		if (pushNotificationSuggestion.isEmpty()) {
			return new ResponseEntity<>(new SuccessResponse(true, FETCH_FAILURE, pushNotificationSuggestion),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new SuccessResponse(false, FETCH_SUCCESS, pushNotificationSuggestion),
					HttpStatus.OK);
		}
	}

	/*
	 * @Author : Hemadri G
	 */
	@Operation(summary = "This API is used to update push notification suggestion")
	@PutMapping("/push-notification-suggestion")
	public ResponseEntity<SuccessResponse> updatePushNotificationSuggestion(
			@RequestBody PushNotificationSuggestionPojo pushNotificationSuggestionPojo) {

		return new ResponseEntity<>(new SuccessResponse(false, UPDATE_SUCCESS,
				pushNotificationSuggestionService.updatePushNotificationSuggestion(pushNotificationSuggestionPojo)),
				HttpStatus.OK);
	}

	/*
	 * @Author : Hemadri G
	 */
	@Operation(summary = "This API is used to delete push notification suggestion")
	@DeleteMapping("/push-notification-suggestion")
	public ResponseEntity<SuccessResponse> deletePushNotificationSuggestion(@RequestParam Long id) {
		return new ResponseEntity<>(new SuccessResponse(false, DELETE_SUCCESS,
				pushNotificationSuggestionService.deletePushNotificationSuggestion(id)), HttpStatus.OK);
	}

	@Operation(summary = "This API will fetch the push notification suggestions")
	@GetMapping("/push-notification-suggestion")
	public ResponseEntity<SuccessResponse> getAllPushNotificationSuggestion() {
		List<PushNotificationSuggestion> pushNotificationSuggestion = pushNotificationSuggestionService
				.getAllPushNotificationSuggestion();
		if (pushNotificationSuggestion.isEmpty()) {
			return new ResponseEntity<>(new SuccessResponse(true, FETCH_FAILURE, pushNotificationSuggestion),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new SuccessResponse(false, FETCH_SUCCESS, pushNotificationSuggestion),
					HttpStatus.OK);
		}
	}

}
