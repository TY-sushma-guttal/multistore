package com.mrmrscart.orderspaymentsservice.feign.service;

import java.util.List;

import com.mrmrscart.orderspaymentsservice.entity.order.OrderInfo;
import com.mrmrscart.orderspaymentsservice.feign.pojo.SupplierOrdersTotalCount;

public interface SupplierOrderReportService {

	long getTotalOrdersCount(String supplierId);

	SupplierOrdersTotalCount getOrdersTotalCount(String supplierId);

	List<Long> getOrdersMonthInfo(int year, String supplierId);

	List<OrderInfo> getOrdersSummary(int year, String supplierId, int pageNumber, int pageSize, String status);

	long getSumOfOrderAmount(String supplierId);

	List<Long> getSumOfOrderAmountMonthInfo(int year, String supplierId);

}
