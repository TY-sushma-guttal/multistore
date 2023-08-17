package com.mrmrscart.userservice.controller.admin;

import static com.mrmrscart.userservice.common.admin.AdminMarketingToolsConstant.ADMIN_MARKETING_TOOL_FETCH_SUCCESS;
import static com.mrmrscart.userservice.common.admin.AdminMarketingToolsConstant.ADMIN_MARKETING_TOOL_GET_FAIL_MESSAGE;
import static com.mrmrscart.userservice.common.admin.AdminMarketingToolsConstant.ADMIN_MARKETING_TOOL_GET_SUCCESS_MESSAGE;
import static com.mrmrscart.userservice.common.admin.AdminMarketingToolsConstant.ADMIN_MARKETING_TOOL_SUCCESS;
import static com.mrmrscart.userservice.common.admin.AdminMarketingToolsConstant.DISABLE_SUBSCRIPTION_SUCCESS_MESSAGE;
import static com.mrmrscart.userservice.common.admin.AdminMarketingToolsConstant.ENABLE_SUBSCRIPTION_SUCCESS_MESSAGE;
import static com.mrmrscart.userservice.common.admin.AdminMarketingToolsConstant.MARKETING_TOOL_APPROVED_MESSAGE;
import static com.mrmrscart.userservice.common.admin.AdminMarketingToolsConstant.MARKETING_TOOL_REJECTED_MESSAGE;
import static com.mrmrscart.userservice.common.admin.AdminMarketingToolsConstant.PRICE_UPDATE_SUCCESS;
import static com.mrmrscart.userservice.common.admin.AdminMarketingToolsConstant.STATUS_UPDATE_SUCCESS;
import static com.mrmrscart.userservice.common.admin.AdminMarketingToolsConstant.SUBSCRIPTION_FOUND_MESSAGE;
import static com.mrmrscart.userservice.common.admin.AdminMarketingToolsConstant.SUBSCRIPTION_NOT_FOUND_MESSAGE;

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

import com.mrmrscart.userservice.entity.admin.EMarketingToolStatus;
import com.mrmrscart.userservice.entity.admin.EMarketingTools;
import com.mrmrscart.userservice.entity.admin.EStoreType;
import com.mrmrscart.userservice.entity.reseller.MarketingToolPurchaseHistory;
import com.mrmrscart.userservice.entity.reseller.UserMarketingTool;
import com.mrmrscart.userservice.entity.supplier.EUserRole;
import com.mrmrscart.userservice.pojo.admin.AdminMarketingToolDisableEnablePojo;
import com.mrmrscart.userservice.pojo.admin.AdminMarketingToolsPojo;
import com.mrmrscart.userservice.pojo.admin.AdminMarketingToolsPricePojo;
import com.mrmrscart.userservice.pojo.admin.IndividualPricingPojo;
import com.mrmrscart.userservice.pojo.admin.MarketingToolSubscriptionPojo;
import com.mrmrscart.userservice.pojo.reseller.MarketingToolCommentPojo;
import com.mrmrscart.userservice.response.admin.SuccessResponse;
import com.mrmrscart.userservice.service.admin.AdminMarketingToolService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/users")
public class AdminMarketingToolController {

	@Autowired
	private AdminMarketingToolService adminMarketingToolService;

	@PostMapping("/admin-marketing-tool")
	@Operation(summary = "This api is used to add Individual Pricing for Marketing Tool")
	public ResponseEntity<SuccessResponse> addIndividualPricing(
			@RequestBody AdminMarketingToolsPojo adminMarketingToolsPojo) {
		return new ResponseEntity<>(new SuccessResponse(false, ADMIN_MARKETING_TOOL_SUCCESS,
				adminMarketingToolService.addIndividualPricing(adminMarketingToolsPojo)), HttpStatus.OK);
	}

	@PutMapping("/admin-marketing-tool-price")
	@Operation(summary = "This api is used to update the Marketing Tool Price")
	public ResponseEntity<SuccessResponse> updatePrice(
			@RequestBody AdminMarketingToolsPricePojo adminMarketingToolsPricePojo) {
		return new ResponseEntity<>(new SuccessResponse(false, PRICE_UPDATE_SUCCESS,
				adminMarketingToolService.updatePrice(adminMarketingToolsPricePojo)), HttpStatus.OK);
	}

	@Operation(summary = "This api to get all marketing tool based on type")
	@GetMapping("/admin-marketing-tool/{pageNumber}/{pageSize}/{marketingToolType}")
	public ResponseEntity<SuccessResponse> getAllUnapprovedMarketingTools(@PathVariable int pageNumber,
			@PathVariable int pageSize, @PathVariable EMarketingTools marketingToolType) {
		return new ResponseEntity<>(new SuccessResponse(false, ADMIN_MARKETING_TOOL_FETCH_SUCCESS,
				adminMarketingToolService.getAllUnapprovedMarketingTools(pageNumber, pageSize, marketingToolType)),
				HttpStatus.OK);
	}

	//
	@Operation(summary = "This API is used for fetching all the individual pricing details created by admin based on the supplier or reseller store")
	@GetMapping("/admin-marketing-tool/individual-pricing")
	public ResponseEntity<SuccessResponse> getIndividualPricing(@RequestParam EUserRole storeType) {
		List<IndividualPricingPojo> individualPricing = adminMarketingToolService.getIndividualPricing(storeType);
		if (!individualPricing.isEmpty()) {
			return new ResponseEntity<>(
					new SuccessResponse(false, ADMIN_MARKETING_TOOL_GET_SUCCESS_MESSAGE, individualPricing),
					HttpStatus.OK);
		} else
			return new ResponseEntity<>(
					new SuccessResponse(true, ADMIN_MARKETING_TOOL_GET_FAIL_MESSAGE, individualPricing), HttpStatus.OK);
	}

	//
	@Operation(summary = "This API is used for approving or rejecting the tool campaigns created by the supplier or reseller")
	@PutMapping("/admin-marketing-tool/approve-reject-campaign")
	public ResponseEntity<SuccessResponse> approveRejectMarketingTool(@RequestParam EMarketingToolStatus status,
			@RequestParam Long marketingToolId, @RequestParam String userId) {
		UserMarketingTool userMarketingTool = adminMarketingToolService.approveRejectMarketingTool(status,
				marketingToolId, userId);
		if (status == EMarketingToolStatus.APPROVED) {
			return new ResponseEntity<>(new SuccessResponse(false, MARKETING_TOOL_APPROVED_MESSAGE, userMarketingTool),
					HttpStatus.OK);
		} else
			return new ResponseEntity<>(new SuccessResponse(false, MARKETING_TOOL_REJECTED_MESSAGE, userMarketingTool),
					HttpStatus.OK);
	}

	//
	@Operation(summary = "This API is used for enabling or disabling the tool subscriptions created by the supplier or reseller")
	@PutMapping("/admin-marketing-tool/tool-subscription")
	public ResponseEntity<SuccessResponse> enableDisableToolSubscription(@RequestParam("purchaseId") Long purchaseId,
			@RequestParam("status") boolean status, @RequestParam("marketingTool") EMarketingTools marketingTool) {
		if (adminMarketingToolService.enableDisableToolSubscription(purchaseId, status, marketingTool)) {
			return new ResponseEntity<>(new SuccessResponse(false, DISABLE_SUBSCRIPTION_SUCCESS_MESSAGE, null),
					HttpStatus.OK);
		} else
			return new ResponseEntity<>(new SuccessResponse(false, ENABLE_SUBSCRIPTION_SUCCESS_MESSAGE, null),
					HttpStatus.OK);
	}

	@PutMapping("/admin-marketing-tool/enable-disable")
	@Operation(summary = "This API is used for enable or disable the marketing tool")
	public ResponseEntity<SuccessResponse> enableOrDisableTool(
			@RequestBody AdminMarketingToolDisableEnablePojo adminMarketingToolDisableEnablePojo) {
		return new ResponseEntity<>(
				new SuccessResponse(false, STATUS_UPDATE_SUCCESS,
						adminMarketingToolService.enableOrDisableTool(adminMarketingToolDisableEnablePojo)),
				HttpStatus.OK);
	}

	//
	@PostMapping("/admin-marketing-tool/tool-subscription/{pageNumber}/{pageSize}")
	@Operation(summary = "This API is used for fetching all the subscriptions based on the marketing tool")
	public ResponseEntity<SuccessResponse> getSubscriptionsByTool(@PathVariable("pageNumber") int pageNumber,
			@PathVariable int pageSize, @RequestBody MarketingToolSubscriptionPojo subscriptionPojo) {
		List<MarketingToolPurchaseHistory> subscriptionsByTool = adminMarketingToolService
				.getSubscriptionsByTool(subscriptionPojo, pageNumber, pageSize);
		if (!subscriptionsByTool.isEmpty()) {
			return new ResponseEntity<>(new SuccessResponse(false, SUBSCRIPTION_FOUND_MESSAGE, subscriptionsByTool),
					HttpStatus.OK);
		} else
			return new ResponseEntity<>(new SuccessResponse(true, SUBSCRIPTION_NOT_FOUND_MESSAGE, subscriptionsByTool),
					HttpStatus.OK);
	}

	//
	@Operation(summary = "This API is used for fetching a enabled individual pricing details based on supplier or reseller")
	@GetMapping("/marketing-tool/individual-pricing/{pageNumber}/{pageSize}")
	public ResponseEntity<SuccessResponse> getEnabledIndividualPricing(@RequestParam("storeType") EUserRole storeType,
			@PathVariable("pageNumber") int pageNumber, @PathVariable("pageSize") int pageSize) {
		List<IndividualPricingPojo> individualPricing = adminMarketingToolService.getEnabledIndividualPricing(storeType,
				pageNumber, pageSize);
		if (!individualPricing.isEmpty()) {
			return new ResponseEntity<>(
					new SuccessResponse(false, ADMIN_MARKETING_TOOL_GET_SUCCESS_MESSAGE, individualPricing),
					HttpStatus.OK);
		} else
			return new ResponseEntity<>(
					new SuccessResponse(true, ADMIN_MARKETING_TOOL_GET_FAIL_MESSAGE, individualPricing), HttpStatus.OK);
	}

	@GetMapping("/admin-marketing-tool/subscription-count")
	@Operation(summary = "This API is used to Get Subscription count based on RESELLER / SUPPLIER ")
	public ResponseEntity<SuccessResponse> getSubscriptionCount(@RequestParam EStoreType type) {
		return new ResponseEntity<>(new SuccessResponse(false, MARKETING_TOOL_REJECTED_MESSAGE,
				adminMarketingToolService.getSubscriptionCount(type)), HttpStatus.OK);
	}

	@Operation(summary = "This API is used for adding comments and attachment in the marketing tool")
	@PutMapping("/marketing-tool/comment")
	public ResponseEntity<SuccessResponse> addComments(@RequestBody MarketingToolCommentPojo marketingToolCommentPojo) {
		return new ResponseEntity<>(new SuccessResponse(false, SUBSCRIPTION_FOUND_MESSAGE,
				adminMarketingToolService.addMarketingToolComments(marketingToolCommentPojo)), HttpStatus.OK);
	}

	@Operation(summary = "This API is used for getting active subscription based on UserType and UserId")
	@GetMapping("/marketing-tool/subscription/{purchasedById}/{purchasedByType}/{adminMarketingToolName}")
	public ResponseEntity<SuccessResponse> getSubscription(@PathVariable String purchasedById,
			@PathVariable String purchasedByType, @PathVariable String adminMarketingToolName) {
		return new ResponseEntity<>(new SuccessResponse(false, SUBSCRIPTION_FOUND_MESSAGE,
				adminMarketingToolService.getSubscription(purchasedById, purchasedByType, adminMarketingToolName)),
				HttpStatus.OK);
	}

}
