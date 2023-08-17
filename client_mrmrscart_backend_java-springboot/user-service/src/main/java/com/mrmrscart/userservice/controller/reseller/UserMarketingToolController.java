package com.mrmrscart.userservice.controller.reseller;

import static com.mrmrscart.userservice.common.reseller.UserMarketingToolConstant.*;
import static com.mrmrscart.userservice.common.reseller.UserMarketingToolConstant.MARKETING_TOOL_DELETE_SUCCESS_MESSAGE;
import static com.mrmrscart.userservice.common.reseller.UserMarketingToolConstant.MARKETING_TOOL_SUBSCRIPTION_FAIL_MESSAGE;
import static com.mrmrscart.userservice.common.reseller.UserMarketingToolConstant.MARKETING_TOOL_SUBSCRIPTION_SUCCESS_MESSAGE;
import static com.mrmrscart.userservice.common.reseller.UserMarketingToolConstant.MARKETING_TOOL_SUCCESS_MESSAGE;

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

import com.mrmrscart.userservice.entity.admin.EMarketingTools;
import com.mrmrscart.userservice.pojo.admin.PurchasedMarketingToolPojo;
import com.mrmrscart.userservice.pojo.admin.PurchasedMarketingToolResponsePojo;
import com.mrmrscart.userservice.pojo.reseller.PurchaseMarketingToolPojo;
import com.mrmrscart.userservice.pojo.reseller.UserMarketingToolPojo;
import com.mrmrscart.userservice.pojo.reseller.UserMarketingToolUpdatePojo;
import com.mrmrscart.userservice.response.admin.SuccessResponse;
import com.mrmrscart.userservice.service.reseller.UserMarketingToolService;

import io.swagger.v3.oas.annotations.Operation;

/**
 * 
 * @author Soumyajit, Sudharshan
 *
 */

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/users")
public class UserMarketingToolController {

	@Autowired
	private UserMarketingToolService userMarketingToolService;

	@PostMapping("/marketing-tool")
	public ResponseEntity<SuccessResponse> addMarketingTool(@RequestBody UserMarketingToolPojo data) {
		return new ResponseEntity<>(new SuccessResponse(false, MARKETING_TOOL_ADDED_SUCCESS_MESSAGE,
				userMarketingToolService.addMarketingTool(data)), HttpStatus.OK);
	}

	//
	@Operation(summary = "This API is used for deleting a marketing tool by the supplier or reseller")
	@DeleteMapping("/marketing-tool")
	public ResponseEntity<SuccessResponse> deleteMarketingTool(@RequestParam("marketingToolId") Long marketingToolId) {
		return new ResponseEntity<>(new SuccessResponse(false, MARKETING_TOOL_DELETE_SUCCESS_MESSAGE,
				userMarketingToolService.deleteMarketingTool(marketingToolId)), HttpStatus.OK);
	}

	//
	@Operation(summary = "This API is used for fetching a marketing tool subscriptions of supplier or reseller")
	@PostMapping("/marketing-tool/subscription-history/{pageNumber}/{pageSize}")
	public ResponseEntity<SuccessResponse> getMarketingToolSubscriptionHistory(
			@RequestBody PurchasedMarketingToolPojo purchasedMarketingToolPojo,
			@PathVariable("pageNumber") int pageNumber, @PathVariable("pageSize") int pageSize) {
		List<PurchasedMarketingToolResponsePojo> allSubscriptions = userMarketingToolService
				.getAllSubscriptions(purchasedMarketingToolPojo, pageNumber, pageSize);
		if (!allSubscriptions.isEmpty()) {
			return new ResponseEntity<>(
					new SuccessResponse(false, MARKETING_TOOL_SUBSCRIPTION_SUCCESS_MESSAGE, allSubscriptions),
					HttpStatus.OK);
		} else
			return new ResponseEntity<>(
					new SuccessResponse(true, MARKETING_TOOL_SUBSCRIPTION_FAIL_MESSAGE, allSubscriptions),
					HttpStatus.OK);
	}

	@Operation(summary = "This method will update the user marketing tool details for admin screen")
	@PutMapping("/marketing-tool")
	public ResponseEntity<SuccessResponse> updateUserMarketingTool(@RequestBody UserMarketingToolUpdatePojo data) {
		return new ResponseEntity<>(new SuccessResponse(false, USER_MARKETING_TOOL_UPDATE_SUCCESS_MESSAGE,
				userMarketingToolService.userMarketingToolUpdate(data)), HttpStatus.OK);
	}

	@Operation(summary = "This API is used to fetch marketing tool campaigns based on userType & toolName")
	@GetMapping("/marketing-tool")
	public ResponseEntity<SuccessResponse> getMarketingTool(@RequestParam String userTypeId,
			@RequestParam EMarketingTools toolType, @RequestParam int pageNumber, @RequestParam int pageSize) {
		return new ResponseEntity<>(
				new SuccessResponse(false, MARKETING_TOOL_SUCCESS_MESSAGE,
						userMarketingToolService.getMarketingTool(userTypeId, toolType, pageNumber, pageSize)),
				HttpStatus.OK);
	}

	@Operation(summary = "This API is used for purchasing a marketing tool or a tool campaign based on the user type")
	@PostMapping("/marketing-tool/purchase-marketing-tool")
	public ResponseEntity<SuccessResponse> purchaseMarketingTool(@RequestBody PurchaseMarketingToolPojo data) {
		return new ResponseEntity<>(new SuccessResponse(false, PURCHASE_TOOL_SUCCESS_MESSAGE,
				userMarketingToolService.purchaseMarketingTool(data)), HttpStatus.OK);
	}

	@Operation(summary = "This API is used for fetching the unlocked marketing tool names")
	@GetMapping("/marketing-tool/tool-status")
	public ResponseEntity<SuccessResponse> validateMarketingToolStatus(@RequestParam("supplierId") String supplierId) {
		return new ResponseEntity<>(
				new SuccessResponse(false, "success", userMarketingToolService.validateMarketingToolStatus(supplierId)),
				HttpStatus.OK);
	}
}
