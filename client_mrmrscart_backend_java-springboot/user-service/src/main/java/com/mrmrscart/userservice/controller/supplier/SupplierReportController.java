package com.mrmrscart.userservice.controller.supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.userservice.pojo.supplier.OrderResponsePojo;
import com.mrmrscart.userservice.response.supplier.SuccessResponse;
import com.mrmrscart.userservice.service.supplier.SupplierReportService;
import static com.mrmrscart.userservice.common.supplier.SupplierConstant.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/users/")
public class SupplierReportController {

	@Autowired
	private SupplierReportService supplierReportService;

	/**
	 * @author Hemadri G
	 * 
	 * @param supplierId
	 * @return
	 */
	@GetMapping("/reports/revenue-sales")
	public ResponseEntity<SuccessResponse> getRevenueSalesTotalCount(@RequestParam String supplierId) {
		return new ResponseEntity<>(new SuccessResponse(false, COUNT_FETCH_SUCCESS,
				supplierReportService.getRevenueSalesTotalCount(supplierId)), HttpStatus.OK);
	}

	/**
	 * @author Hemadri G
	 * @param year
	 * @param supplierId
	 * @return
	 */
	@GetMapping("/reports/revenue-sales/referrals")
	public ResponseEntity<SuccessResponse> getRevenueSalesReferrals(@RequestParam int year,
			@RequestParam String supplierId) {
		return new ResponseEntity<>(new SuccessResponse(false, COUNT_FETCH_SUCCESS,
				supplierReportService.getRevenueSalesReferrals(year, supplierId)), HttpStatus.OK);
	}

	/**
	 * @author Hemadri G
	 * 
	 * @param year
	 * @param storeCode
	 * @return
	 */
	@GetMapping("/reports/revenue-sales/customers")
	public ResponseEntity<SuccessResponse> getRevenueSalesCustomers(@RequestParam int year,
			@RequestParam String storeCode) {
		return new ResponseEntity<>(new SuccessResponse(false, COUNT_FETCH_SUCCESS,
				supplierReportService.getRevenueSalesCustomers(year, storeCode)), HttpStatus.OK);
	}

	/**
	 * @author Hemadri G
	 * 
	 * @param year
	 * @param supplierId
	 * @return
	 */
	@GetMapping("/reports/revenue-sales/sales")
	public ResponseEntity<SuccessResponse> getSumOfOrderAmountMonthInfo(@RequestParam int year,
			@RequestParam String supplierId) {
		return supplierReportService.getSumOfOrderAmountMonthInfo(year, supplierId);

	}

	/**
	 * @author Hemadri G
	 * 
	 * @param supplierId
	 * @return
	 */
	@GetMapping("/reports/orders")
	public ResponseEntity<SuccessResponse> getOrdersTotalCount(@RequestParam String supplierId) {
		return supplierReportService.getOrdersTotalCount(supplierId);
	}

	/**
	 * @author Hemadri G
	 * 
	 * @param year
	 * @param supplierId
	 * @return
	 */
	@GetMapping("/reports/orders/info")
	public ResponseEntity<SuccessResponse> getOrdersMonthInfo(@RequestParam int year, @RequestParam String supplierId) {
		return supplierReportService.getOrdersMonthInfo(year, supplierId);
	}

	@GetMapping("/reports/orders/summary")
	public ResponseEntity<SuccessResponse> getOrdersSummary(@RequestParam int year, @RequestParam String supplierId,
			@RequestParam int pageNumber, @RequestParam int pageSize, @RequestParam(required = false) String status) {
		List<OrderResponsePojo> ordersSummary = supplierReportService.getOrdersSummary(year, supplierId, pageNumber,
				pageSize, status);
		if (ordersSummary.isEmpty()) {
			return new ResponseEntity<>(new SuccessResponse(true, ORDER_FETCH_FAILURE, ordersSummary), HttpStatus.OK);
		}
		return new ResponseEntity<>(new SuccessResponse(false, ORDER_FETCH_SUCCESS, ordersSummary), HttpStatus.OK);
	}
}
