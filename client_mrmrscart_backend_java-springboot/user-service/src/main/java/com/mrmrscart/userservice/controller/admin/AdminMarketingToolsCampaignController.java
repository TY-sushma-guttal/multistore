package com.mrmrscart.userservice.controller.admin;

import static com.mrmrscart.userservice.common.admin.AdminMarketingToolsConstant.*;
import static com.mrmrscart.userservice.common.admin.AdminMarketingToolsConstant.FETCH_FAILURE;
import static com.mrmrscart.userservice.common.admin.AdminMarketingToolsConstant.FETCH_SUCCESS;

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

import com.mrmrscart.userservice.entity.admin.EStoreType;
import com.mrmrscart.userservice.pojo.admin.AdminMarketingToolsCampaignPojo;
import com.mrmrscart.userservice.pojo.admin.AdminMarketingToolsCampaignResponsePojo;
import com.mrmrscart.userservice.pojo.admin.AdminMarketingToolsCampaignUpdatePojo;
import com.mrmrscart.userservice.pojo.admin.AdminToolCampaignIdAndStatusPojo;
import com.mrmrscart.userservice.pojo.reseller.AdminCampaignToolFilterPojo;
import com.mrmrscart.userservice.response.admin.SuccessResponse;
import com.mrmrscart.userservice.service.admin.AdminMarketingToolsCampaignService;
import com.mrmrscart.userservice.wrapper.admin.AdminMarketingToolsWrapper;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/users")
public class AdminMarketingToolsCampaignController {

	@Autowired
	private AdminMarketingToolsCampaignService adminMarketingToolsCampaignService;

	@Operation(summary = "This Api is used to get all tools based on days & store type ADMIN-MARKETING_TOOLS-SET_TOOL_PRICING-CREATE_DISCOUNT-TOOLS_DROPDOWN")
	@GetMapping("/admin-marketing-tool-campaign/tools-dropdown")
	public ResponseEntity<SuccessResponse> getAdminMarketingTools(@RequestParam String days,
			@RequestParam EStoreType storeType) {
		List<AdminMarketingToolsWrapper> adminMarketingTools = adminMarketingToolsCampaignService
				.getAdminMarketingTools(days, storeType);
		if (!adminMarketingTools.isEmpty()) {
			return new ResponseEntity<>(new SuccessResponse(false, FETCH_SUCCESS, adminMarketingTools), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new SuccessResponse(true, FETCH_FAILURE, adminMarketingTools), HttpStatus.OK);
		}
	}

	@PostMapping("/admin-marketing-tool-campaign")
	@Operation(summary = "This api is used to create Marketing Tool Campaign")
	public ResponseEntity<SuccessResponse> createAdminMarketingToolCampaign(
			@RequestBody AdminMarketingToolsCampaignPojo adminMarketingToolsCampaignPojo) {
		return new ResponseEntity<>(new SuccessResponse(false, ADMIN_MARKETING_TOOL_CAMPAIGN_SUCCESS,
				adminMarketingToolsCampaignService.createAdminMarketingToolCampaign(adminMarketingToolsCampaignPojo)),
				HttpStatus.OK);
	}

	@Operation(summary = "This api is used to update the field the admin marketing tool campaign. ")
	@PutMapping("/admin-marketing-tool-campaign")
	public ResponseEntity<SuccessResponse> updateToolCampaign(@RequestBody AdminMarketingToolsCampaignUpdatePojo data) {
		return new ResponseEntity<>(new SuccessResponse(false, ADMIN_MARKETING_TOOL_CAMPAIGN_UPDATE_SUCCESS_MESSAGE,
				adminMarketingToolsCampaignService.updateCampaignTool(data)), HttpStatus.OK);
	}

	@Operation(summary = "This api will enable or disable the admin marketing tool campaign. ")
	@PutMapping("/admin-marketing-tool-campaign-status")
	public ResponseEntity<SuccessResponse> enableDisableToolCampaign(
			@RequestBody AdminToolCampaignIdAndStatusPojo data) {
		return new ResponseEntity<>(new SuccessResponse(false, ADMIN_MARKETING_TOOL_CAMPAIGN_UPDATE_SUCCESS_MESSAGE,
				adminMarketingToolsCampaignService.enableDisableCampaign(data)), HttpStatus.OK);
	}

	@Operation(summary = "This api will fetch all the tool campaign based on store type. ")
	@GetMapping("/admin-marketing-tool-campaign/{pageNumber}/{pageSize}")
	public ResponseEntity<SuccessResponse> enableDisableToolCampaign(@RequestParam String storeType,
			@PathVariable int pageNumber, @PathVariable int pageSize) {
		return new ResponseEntity<>(
				new SuccessResponse(false, ADMIN_MARKETING_TOOL_CAMPAIGN_UPDATE_SUCCESS_MESSAGE,
						adminMarketingToolsCampaignService.getAllAdminCampaignTool(storeType, pageNumber, pageSize)),
				HttpStatus.OK);
	}

	/**
	 * 
	 * @author Soumyajit Sarkar
	 * 
	 */

	@Operation(summary = "This api will fetch all the filtered data")
	@GetMapping("/admin-marketing-tool-campaign/filter")
	public ResponseEntity<SuccessResponse> marketingToolCampaign(@RequestBody AdminCampaignToolFilterPojo data) {
		return new ResponseEntity<>(new SuccessResponse(false, "Filtered Data Fetched Successfully",
				adminMarketingToolsCampaignService.getFilteredAdminCampaignTool(data)), HttpStatus.OK);
	}

	@Operation(summary = "This API will fetch all the tool Campaign Based on Store type & days SUPPLIER/RESELLER-UNLOCK_TOOLS-COMBO")
	@GetMapping("/marketing-tool/admin-marketing-tool-campaign/{pageNumber}/{pageSize}")
	public ResponseEntity<SuccessResponse> getEnabledAdminMarketingToolCampaign(
			@RequestParam(required = false) String days, @RequestParam String storeType, @PathVariable int pageNumber,
			@PathVariable int pageSize) {
		List<AdminMarketingToolsCampaignResponsePojo> adminMarketingToolsCampaignResponsePojos = adminMarketingToolsCampaignService
				.getEnabledAdminMarketingToolCampaign(days, storeType, pageNumber, pageSize);
		if (adminMarketingToolsCampaignResponsePojos.isEmpty()) {
			return new ResponseEntity<>(new SuccessResponse(true, ADMIN_MARKETING_TOOL_CAMPAIGN_GET_FAIL_MESSAGE,
					adminMarketingToolsCampaignResponsePojos), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new SuccessResponse(false, ADMIN_MARKETING_TOOL_FETCH_SUCCESS,
					adminMarketingToolsCampaignResponsePojos), HttpStatus.OK);
		}
	}
}
