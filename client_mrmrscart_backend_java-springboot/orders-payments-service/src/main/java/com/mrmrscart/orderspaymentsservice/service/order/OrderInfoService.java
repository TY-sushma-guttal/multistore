package com.mrmrscart.orderspaymentsservice.service.order;

import java.util.List;

import com.mrmrscart.orderspaymentsservice.entity.order.OrderInfo;
import com.mrmrscart.orderspaymentsservice.enums.ECommission;
import com.mrmrscart.orderspaymentsservice.enums.EOrderInfoFilter;
import com.mrmrscart.orderspaymentsservice.pojo.order.ConfirmOrderPojo;
import com.mrmrscart.orderspaymentsservice.pojo.order.OrderInfoPojo;
import com.mrmrscart.orderspaymentsservice.pojo.order.SerialNoUpdatePojo;

public interface OrderInfoService {
	public OrderInfo createOrder(OrderInfoPojo orderInfoPojos);

	public List<OrderInfo> getOrders(ECommission commissionType, EOrderInfoFilter filterType, String keyword,
			String supplierId, String orderStatus, int pageNumber, int pageSize);

	public OrderInfo updateSerialNo(SerialNoUpdatePojo serialNoUpdatePojo);

	public List<OrderInfo> getOrderByStatus(String supplierId, String orderStatus, int pageNumber, int pageSize,
			String modeOfOrder);

	public OrderInfo rejectOrder(String orderId);

	public List<OrderInfo> confirmOrder(ConfirmOrderPojo confirmOrderPojo);

}
