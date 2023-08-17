package com.mrmrscart.orderspaymentsservice.repository.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mrmrscart.orderspaymentsservice.entity.order.OrderInfo;

public interface OrderInfoRepository extends JpaRepository<OrderInfo, String>{

	List<OrderInfo> findByProductOwnerIdAndOrderStatus(String productOwnerId,String orderStatus);
	
	List<OrderInfo> findByProductOwnerIdAndOrderStatusAndSkuIdIgnoreCaseContaining(String productOwnerId,String orderStatus,String skuId);
	
	List<OrderInfo> findByProductOwnerIdAndOrderStatusAndModeOfOrderIgnoreCaseContaining(String productOwnerId,String orderStatus,String modeOfOrder);
	
	OrderInfo findByOrderIdAndOrderStatus(String orderId,String orderStatus);
	
	long countByProductOwnerId(String productOwnerId);
	
	long countByProductOwnerIdAndOrderStatus(String productOwnerId,String orderStatus);
	
	@Query("Select c from OrderInfo c where c.productOwnerId=?1 and  EXTRACT (year FROM c.orderDate) =?2 and EXTRACT (month FROM c.orderDate) =?3 ")
	List<OrderInfo> findByMonthYear(String productOwnerId,int year,int month);
	
	@Query("Select c from OrderInfo c where c.productOwnerId=?1 and  EXTRACT (year FROM c.orderDate) =?2 and c.orderStatus= ?3 ")
	List<OrderInfo> findByYearWithStatus(String productOwnerId,int year,String orderStatus);
	
	@Query("Select c from OrderInfo c where c.productOwnerId=?1 and  EXTRACT (year FROM c.orderDate) =?2 ")
	List<OrderInfo> findByYear(String productOwnerId,int year);
	
	@Query("Select sum(c.orderAmount) from OrderInfo c where c.productOwnerId=?1")
	long sumOfOrderAmount(String productOwnerId);
	
	@Query("Select c from OrderInfo c where c.productOwnerId=?1 and  EXTRACT (year FROM c.orderDate) =?2 and EXTRACT (month FROM c.orderDate) =?3 ")
	List<OrderInfo> sumOfOrderAmountByMonthYear(String productOwnerId,int year,int month);
	
}
