package com.mrmrscart.orderspaymentsservice.controller.order;

import static com.mrmrscart.orderspaymentsservice.common.order.OrderConstant.FETCH_FAILURE;
import static com.mrmrscart.orderspaymentsservice.common.order.OrderConstant.FETCH_SUCCESS;
import static com.mrmrscart.orderspaymentsservice.common.order.OrderConstant.ORDER_CONFIRM_FAILURE;
import static com.mrmrscart.orderspaymentsservice.common.order.OrderConstant.ORDER_CONFIRM_SUCCESS;
import static com.mrmrscart.orderspaymentsservice.common.order.OrderConstant.ORDER_REJECT_FAILURE;
import static com.mrmrscart.orderspaymentsservice.common.order.OrderConstant.ORDER_REJECT_SUCCESS;
import static com.mrmrscart.orderspaymentsservice.common.order.OrderConstant.SERIAL_NO_UPDATE_SUCCESS;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.orderspaymentsservice.entity.order.OrderInfo;
import com.mrmrscart.orderspaymentsservice.enums.ECommission;
import com.mrmrscart.orderspaymentsservice.enums.EOrderInfoFilter;
import com.mrmrscart.orderspaymentsservice.pojo.order.ConfirmOrderPojo;
import com.mrmrscart.orderspaymentsservice.pojo.order.OrderInfoPojo;
import com.mrmrscart.orderspaymentsservice.pojo.order.SerialNoUpdatePojo;
import com.mrmrscart.orderspaymentsservice.response.order.SuccessResponse;
import com.mrmrscart.orderspaymentsservice.service.order.OrderInfoService;

@RestController
@RequestMapping("/api/v1/order-payment")
@CrossOrigin(origins = "*")
public class OrderInfoController {

	@Autowired
	private OrderInfoService service;

	@PostMapping("/order")
	public ResponseEntity<SuccessResponse> createOrder(@RequestBody OrderInfoPojo orderInfoPojo) {
		service.createOrder(orderInfoPojo);
		return null;

	}

	/**
	 * @author Hemadri G
	 * 
	 * @param commissionType
	 * @param filterType
	 * @param keyword
	 * @param supplierId
	 * @param orderStatus
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@PostMapping("/supplier/init-orders")
	public ResponseEntity<SuccessResponse> getOrders(@RequestParam(required = false) ECommission commissionType,
			@RequestParam(required = false) EOrderInfoFilter filterType, @RequestParam(required = false) String keyword,
			@RequestParam String supplierId, @RequestParam String orderStatus, @RequestParam int pageNumber,
			@RequestParam int pageSize) {
		return new ResponseEntity<>(new SuccessResponse(false, FETCH_SUCCESS,
				service.getOrders(commissionType, filterType, keyword, supplierId, orderStatus, pageNumber, pageSize)),
				HttpStatus.OK);
	}

	/**
	 * @author Hemadri G
	 * 
	 * @param serialNoUpdatePojo
	 * @return
	 */
	@PutMapping("/supplier/orders/serial-no")
	public ResponseEntity<SuccessResponse> updateSerialNo(@RequestBody SerialNoUpdatePojo serialNoUpdatePojo) {
		return new ResponseEntity<>(
				new SuccessResponse(false, SERIAL_NO_UPDATE_SUCCESS, service.updateSerialNo(serialNoUpdatePojo)),
				HttpStatus.OK);
	}

	/**
	 * @author Hemadri G
	 * 
	 * 
	 * @param orderId
	 * @return
	 */
	@PutMapping("/supplier/reject-order")
	public ResponseEntity<SuccessResponse> rejectOrder(@RequestParam String orderId) {
		OrderInfo rejectOrder = service.rejectOrder(orderId);
		if (rejectOrder != null) {
			return new ResponseEntity<>(new SuccessResponse(false, ORDER_REJECT_SUCCESS, null), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new SuccessResponse(true, ORDER_REJECT_FAILURE, null), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * @author Hemadri G
	 * 
	 * 
	 * @param confirmOrderPojo
	 * @return
	 */
	@PutMapping("/supplier/confirm-order")
	public ResponseEntity<SuccessResponse> confirmOrder(@RequestBody ConfirmOrderPojo confirmOrderPojo) {
		List<OrderInfo> confirmOrder = service.confirmOrder(confirmOrderPojo);
		if (!confirmOrder.isEmpty()) {
			return new ResponseEntity<>(new SuccessResponse(false, ORDER_CONFIRM_SUCCESS, null), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new SuccessResponse(true, ORDER_CONFIRM_FAILURE, null), HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * @author Hemadri G
	 * 
	 * @param supplierId
	 * @param orderStatus
	 * @param pageNumber
	 * @param pageSize
	 * @param modeOfOrder
	 * @return
	 */
	@PostMapping("/supplier/orders")
	public ResponseEntity<SuccessResponse> getOrderByStatus(@RequestParam String supplierId,
			@RequestParam String orderStatus, @RequestParam int pageNumber, @RequestParam int pageSize,
			@RequestParam(required = false) String modeOfOrder) {
		List<OrderInfo> orderByStatus = service.getOrderByStatus(supplierId, orderStatus, pageNumber, pageSize,
				modeOfOrder);
		if (orderByStatus.isEmpty()) {
			return new ResponseEntity<>(new SuccessResponse(true, FETCH_FAILURE, modeOfOrder), HttpStatus.OK);
		}
		return new ResponseEntity<>(new SuccessResponse(false, FETCH_SUCCESS, modeOfOrder), HttpStatus.OK);
	}

}
