package com.mrmrscart.userservice.service.supplier;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.mrmrscart.userservice.pojo.supplier.OrderResponsePojo;
import com.mrmrscart.userservice.pojo.supplier.RevenueSalesTotalCountPojo;
import com.mrmrscart.userservice.response.supplier.SuccessResponse;

public interface SupplierReportService {

	RevenueSalesTotalCountPojo getRevenueSalesTotalCount(String supplierId);

	List<Long> getRevenueSalesReferrals(int year,String supplierId);

	List<Long> getRevenueSalesCustomers(int year, String storeCode);

	ResponseEntity<SuccessResponse> getOrdersTotalCount(String supplierId);

	ResponseEntity<SuccessResponse> getOrdersMonthInfo(int year, String supplierId);

	List<OrderResponsePojo> getOrdersSummary(int year, String supplierId, int pageNumber, int pageSize, String status);

	ResponseEntity<SuccessResponse> getSumOfOrderAmountMonthInfo(int year, String supplierId);


}
