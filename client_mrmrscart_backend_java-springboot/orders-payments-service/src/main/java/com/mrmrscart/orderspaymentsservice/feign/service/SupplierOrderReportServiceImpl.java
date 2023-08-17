package com.mrmrscart.orderspaymentsservice.feign.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrmrscart.orderspaymentsservice.entity.order.OrderInfo;
import com.mrmrscart.orderspaymentsservice.exception.order.OrderException;
import com.mrmrscart.orderspaymentsservice.feign.pojo.SupplierOrdersTotalCount;
import com.mrmrscart.orderspaymentsservice.repository.order.OrderInfoRepository;
import com.mrmrscart.orderspaymentsservice.util.PaginatedResponse;

import static com.mrmrscart.orderspaymentsservice.common.order.OrderConstant.*;

import java.util.ArrayList;
import java.util.List;
@Service
public class SupplierOrderReportServiceImpl implements SupplierOrderReportService{
	
	@Autowired
	private OrderInfoRepository orderInfoRepository;

	@Override
	public long getTotalOrdersCount(String supplierId) {

		try {
			return orderInfoRepository.countByProductOwnerId(supplierId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OrderException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public SupplierOrdersTotalCount getOrdersTotalCount(String supplierId) {
		try {
			SupplierOrdersTotalCount supplierOrdersTotalCount=new SupplierOrdersTotalCount();
			supplierOrdersTotalCount.setTotalOrders(orderInfoRepository.countByProductOwnerId(supplierId));
			supplierOrdersTotalCount.setOrdersCompleted(orderInfoRepository.countByProductOwnerIdAndOrderStatus(supplierId, "COMPLETED"));
			supplierOrdersTotalCount.setOrdersCancelled(orderInfoRepository.countByProductOwnerIdAndOrderStatus(supplierId, "CANCELLED"));
			supplierOrdersTotalCount.setOrdersPending(orderInfoRepository.countByProductOwnerIdAndOrderStatus(supplierId, "PENDING"));
			return supplierOrdersTotalCount;
		} catch (Exception e) {
			e.printStackTrace();
			throw new OrderException(SOMETHING_WENT_WRONG);
		}
		
	}

	@Override
	public List<Long> getOrdersMonthInfo(int year, String supplierId) {

		try {
			List<Long> orders=new ArrayList<>();
			for (int i = 1; i <= 12; i++) {
				orders.add((long)orderInfoRepository.findByMonthYear(supplierId, year, i).size());
			}
			return orders;
		} catch (Exception e) {
			e.printStackTrace();
			throw new OrderException(SOMETHING_WENT_WRONG);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OrderInfo> getOrdersSummary(int year, String supplierId, int pageNumber, int pageSize, String status) {
       try {
		if(status!=null) {
			List<OrderInfo> findByYearWithStatus = orderInfoRepository.findByYearWithStatus(supplierId, year, status);
			return (List<OrderInfo>)PaginatedResponse.getPaginatedResponse(findByYearWithStatus, pageNumber, pageSize);
		}else {
			List<OrderInfo> findByYear = orderInfoRepository.findByYear(supplierId, year);
			return (List<OrderInfo>)PaginatedResponse.getPaginatedResponse(findByYear, pageNumber, pageSize);
		}
	} catch (Exception e) {
		e.printStackTrace();
		throw new OrderException(SOMETHING_WENT_WRONG);
	}		
	}

	@Override
	public long getSumOfOrderAmount(String supplierId) {
		
		try {
			return orderInfoRepository.sumOfOrderAmount(supplierId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OrderException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<Long> getSumOfOrderAmountMonthInfo(int year, String supplierId) {
		try {
			List<Long> list=new ArrayList<>();
			for (int i = 1; i <= 12; i++) {
				List<OrderInfo> sumOfOrderAmountByMonthYear = orderInfoRepository.sumOfOrderAmountByMonthYear(supplierId, year, i);
				long sum=0;
				for (OrderInfo orderInfo : sumOfOrderAmountByMonthYear) {
					sum+=orderInfo.getOrderAmount();
				}
				list.add(sum);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new OrderException(SOMETHING_WENT_WRONG);
		}
	}

}
