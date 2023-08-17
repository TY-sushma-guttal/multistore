package com.mrmrscart.orderspaymentsservice.feign.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.orderspaymentsservice.feign.service.SupplierOrderReportService;
import com.mrmrscart.orderspaymentsservice.response.order.SuccessResponse;
import static com.mrmrscart.orderspaymentsservice.common.order.OrderConstant.*;

@RestController
@RequestMapping("/api/v1/order-payment")
@CrossOrigin(origins = "*")
public class SupplierOrderReportController {
	
	@Autowired
	private SupplierOrderReportService supplierOrderReportService;

	/**
	 * @author Hemadri G
	 * 
	 * @param supplierId
	 * @return
	 */
	@GetMapping("/reports/revenue-sales")
	public ResponseEntity<SuccessResponse> getTotalOrdersCount(@RequestParam String supplierId){
		return new ResponseEntity<>(new SuccessResponse(false, FETCH_SUCCESS, supplierOrderReportService.getTotalOrdersCount(supplierId)),HttpStatus.OK);
	}
	
	/**
	 * @author Hemadri G 
	 * 
	 * @param supplierId
	 * @return
	 */
	@GetMapping("/reports/revenue-sales/amount")
	public ResponseEntity<SuccessResponse> getSumOfOrderAmount(@RequestParam String supplierId){
		return new ResponseEntity<>(new SuccessResponse(false, FETCH_SUCCESS, supplierOrderReportService.getSumOfOrderAmount(supplierId)),HttpStatus.OK);
	}
	
	/**
	 * @author Hemadri G
	 * 
	 * @param year
	 * @param supplierId
	 * @return
	 */
	@GetMapping("/reports/revenue-sales/sales")
	public ResponseEntity<SuccessResponse> getSumOfOrderAmountMonthInfo(@RequestParam int year,@RequestParam String supplierId){
		return new ResponseEntity<>(new SuccessResponse(false, FETCH_SUCCESS, supplierOrderReportService.getSumOfOrderAmountMonthInfo(year,supplierId)),HttpStatus.OK);
	}
	
	/**
	 * @author Hemadri G
	 * 
	 * @param supplierId
	 * @return
	 */
	@GetMapping("/reports/orders")
	public ResponseEntity<SuccessResponse> getOrdersTotalCount(@RequestParam String supplierId){
		return new ResponseEntity<>(new SuccessResponse(false, FETCH_SUCCESS,supplierOrderReportService.getOrdersTotalCount(supplierId)),HttpStatus.OK);
	}
	
	/**
	 * @author Hemadri G
	 * 
	 * @param year
	 * @param supplierId
	 * @return
	 */
	@GetMapping("/reports/orders/info")
	public ResponseEntity<SuccessResponse> getOrdersMonthInfo(@RequestParam int year,@RequestParam String supplierId){
		return new ResponseEntity<>(new SuccessResponse(false, FETCH_SUCCESS,supplierOrderReportService.getOrdersMonthInfo(year,supplierId)),HttpStatus.OK);
	}
	
	/**
	 * @author Hemadri G
	 * 
	 * @param year
	 * @param supplierId
	 * @param pageNumber
	 * @param pageSize
	 * @param status
	 * @return
	 */
	@GetMapping("/reports/orders/summary")
	public ResponseEntity<SuccessResponse> getOrdersSummary(@RequestParam int year,@RequestParam String supplierId,@RequestParam int pageNumber,
			@RequestParam int pageSize, @RequestParam(required = false) String status){
		return new ResponseEntity<>(new SuccessResponse(false, FETCH_SUCCESS,supplierOrderReportService.getOrdersSummary(year,supplierId,pageNumber,pageSize,status)),HttpStatus.OK);
	}
}
