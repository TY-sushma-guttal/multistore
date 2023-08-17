package com.mrmrscart.notificationreportlogservice.controller;

import static com.mrmrscart.notificationreportlogservice.common.MarketingToolPriceChangeConstant.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.notificationreportlogservice.entity.MarketingToolPriceChange;
import com.mrmrscart.notificationreportlogservice.pojo.MarketingToolPriceChangePojo;
import com.mrmrscart.notificationreportlogservice.pojo.PriceChangeHistoryPojo;
import com.mrmrscart.notificationreportlogservice.response.SuccessResponse;
import com.mrmrscart.notificationreportlogservice.service.MarketingToolPriceChangeService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/notification")
public class MarketingToolPriceChangeController {

	@Autowired
	private MarketingToolPriceChangeService marketingToolPriceChangeService;

	@PostMapping("/marketing-tool-price-change")
	public ResponseEntity<SuccessResponse> addMarketingToolPriceChange(
			@RequestBody MarketingToolPriceChangePojo marketingToolPriceChangePojo) {
		return new ResponseEntity<>(
				new SuccessResponse(false, SUCCESS,
						marketingToolPriceChangeService.addMarketingToolPriceChange(marketingToolPriceChangePojo)),
				HttpStatus.OK);
	}

	//
	@Operation(summary = "This API is used for fetching all the price change history of the individual tool pricing and campaign tool pricing")
	@PostMapping("/admin-marketing-tool/price-change-history/{pageNumber}/{pageSize}")
	public ResponseEntity<SuccessResponse> viewPriceChangeHistory(
			@RequestBody PriceChangeHistoryPojo priceChangeHistoryPojo, @PathVariable("pageNumber") int pageNumber,
			@PathVariable("pageSize") int pageSize) {
		List<MarketingToolPriceChange> priceChangeHistory = marketingToolPriceChangeService
				.viewPriceChangeHistory(priceChangeHistoryPojo, pageNumber, pageSize);
		if (!priceChangeHistory.isEmpty()) {
			return new ResponseEntity<>(
					new SuccessResponse(false, PRICE_CHANGE_HISTORY_SUCCESS_MESSAGE, priceChangeHistory),
					HttpStatus.OK);
		} else
			return new ResponseEntity<>(
					new SuccessResponse(true, PRICE_CHANGE_HISTORY_FAIL_MESSAGE, priceChangeHistory), HttpStatus.OK);
	}
}
