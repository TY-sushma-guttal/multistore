package com.mrmrscart.userservice.feign.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.mrmrscart.userservice.feign.pojo.MarketingToolPriceChangePojo;
import com.mrmrscart.userservice.feign.pojo.SupplierChangesHistoryPojo;
import com.mrmrscart.userservice.response.supplier.SuccessResponse;


@FeignClient(name="notification-report-log-service")
public interface NotificationReportLogService {
	
	@PostMapping("/api/v1/notification/supplier-changes-history")
	public ResponseEntity<SuccessResponse> addSupplierChangesHistory(@RequestHeader String userId, @RequestBody List<SupplierChangesHistoryPojo> supplierChangesHistorypojo);

	@PostMapping("/api/v1/notification/marketing-tool-price-change")
	public ResponseEntity<SuccessResponse> addMarketingToolPriceChange(@RequestBody MarketingToolPriceChangePojo marketingToolPriceChangePojo);
}
