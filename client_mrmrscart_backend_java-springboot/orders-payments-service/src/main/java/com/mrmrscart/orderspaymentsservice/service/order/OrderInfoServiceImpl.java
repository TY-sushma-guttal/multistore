package com.mrmrscart.orderspaymentsservice.service.order;

import static com.mrmrscart.orderspaymentsservice.common.order.OrderConstant.INVALID_ORDERID;
import static com.mrmrscart.orderspaymentsservice.common.order.OrderConstant.PLEASE_PROVIDE;
import static com.mrmrscart.orderspaymentsservice.common.order.OrderConstant.SAME_MODE_ERROR;
import static com.mrmrscart.orderspaymentsservice.common.order.OrderConstant.SERIAL_NO;
import static com.mrmrscart.orderspaymentsservice.common.order.OrderConstant.SOMETHING_WENT_WRONG;
import static com.mrmrscart.orderspaymentsservice.common.order.OrderConstant.SUPPLIER_SHIPMENT_ERROR;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mrmrscart.orderspaymentsservice.entity.order.EUserType;
import com.mrmrscart.orderspaymentsservice.entity.order.LogisticInfo;
import com.mrmrscart.orderspaymentsservice.entity.order.OrderInfo;
import com.mrmrscart.orderspaymentsservice.entity.order.OrderQuantityInfo;
import com.mrmrscart.orderspaymentsservice.enums.ECommission;
import com.mrmrscart.orderspaymentsservice.enums.EModeOfOrder;
import com.mrmrscart.orderspaymentsservice.enums.EOrderInfoFilter;
import com.mrmrscart.orderspaymentsservice.enums.EOrderStatus;
import com.mrmrscart.orderspaymentsservice.exception.order.AddressNotFoundException;
import com.mrmrscart.orderspaymentsservice.exception.order.CustomerNotFoundException;
import com.mrmrscart.orderspaymentsservice.exception.order.InvalidCouponException;
import com.mrmrscart.orderspaymentsservice.exception.order.OrderException;
import com.mrmrscart.orderspaymentsservice.exception.order.ProductNotFoundException;
import com.mrmrscart.orderspaymentsservice.exception.order.SupplierNotFoundException;
import com.mrmrscart.orderspaymentsservice.exception.order.UserProfileNotFoundException;
import com.mrmrscart.orderspaymentsservice.feign.client.ProductServiceClient;
import com.mrmrscart.orderspaymentsservice.feign.client.UserServiceClient;
import com.mrmrscart.orderspaymentsservice.feign.pojo.CustomerRegistration;
import com.mrmrscart.orderspaymentsservice.feign.pojo.ESupplierStatus;
import com.mrmrscart.orderspaymentsservice.feign.pojo.MasterProductPojo;
import com.mrmrscart.orderspaymentsservice.feign.pojo.OrderedProductsPojo;
import com.mrmrscart.orderspaymentsservice.feign.pojo.OrdersRequestPojo;
import com.mrmrscart.orderspaymentsservice.feign.pojo.ProductVariationPojo;
import com.mrmrscart.orderspaymentsservice.feign.pojo.SupplierRegistration;
import com.mrmrscart.orderspaymentsservice.feign.pojo.SupplierStoreCoupon;
import com.mrmrscart.orderspaymentsservice.feign.pojo.UserAddressDetails;
import com.mrmrscart.orderspaymentsservice.feign.pojo.UserProfile;
import com.mrmrscart.orderspaymentsservice.feign.response.CustomerRegistrationResponse;
import com.mrmrscart.orderspaymentsservice.feign.response.ProductsResponse;
import com.mrmrscart.orderspaymentsservice.pojo.order.ApplyCouponPojo;
import com.mrmrscart.orderspaymentsservice.pojo.order.ConfirmOrderPojo;
import com.mrmrscart.orderspaymentsservice.pojo.order.IncreaseStockQuantityPojo;
import com.mrmrscart.orderspaymentsservice.pojo.order.OrderInfoPojo;
import com.mrmrscart.orderspaymentsservice.pojo.order.OrderedProductInfoPojo;
import com.mrmrscart.orderspaymentsservice.pojo.order.SerialNoUpdatePojo;
import com.mrmrscart.orderspaymentsservice.repository.order.OrderInfoRepository;
import com.mrmrscart.orderspaymentsservice.response.order.SuccessResponse;
import com.mrmrscart.orderspaymentsservice.response.order.SupplierRegistartionGetResponse;
import com.mrmrscart.orderspaymentsservice.util.PaginatedResponse;

import feign.FeignException;

@Service
public class OrderInfoServiceImpl implements OrderInfoService {

	@Autowired
	private OrderInfoRepository orderInfoRepository;

	@Autowired
	private UserServiceClient userServiceClient;
		
	@Autowired
	private ProductServiceClient productServiceClient;

	@Override
	public OrderInfo createOrder(OrderInfoPojo orderInfoPojo) {

		if (orderInfoPojo.getOrderedStoreType() == EUserType.SUPPLIER) {

			CustomerRegistration findCustomer = findCustomer(orderInfoPojo.getOrderedById());
			validateCustomerDetails(findCustomer, orderInfoPojo);
			SupplierRegistration supplierInfo = findSupplierInfo(orderInfoPojo.getProductOwnerId());
			validateSupplier(supplierInfo, orderInfoPojo);
			List<MasterProductPojo> productInfo = findProductInfo(orderInfoPojo.getProductInfos());
			validateProductDetails(productInfo, orderInfoPojo.getProductOwnerId(), orderInfoPojo);
			OrderInfo orderInfo = new OrderInfo();

			BeanUtils.copyProperties(orderInfoPojo, orderInfo);

		} else if (orderInfoPojo.getOrderedStoreType() == EUserType.RESELLER) {

			throw new OrderException("Provide Store Type As Supplier");

		} else {
			throw new OrderException("Provide Store Type As Supplier");
		}
		return null;
	}

	private List<MasterProductPojo> findProductInfo(List<OrderedProductInfoPojo> list) {

		try {
			OrderedProductsPojo orderedProductsPojo = new OrderedProductsPojo();
			orderedProductsPojo
					.setProductId(list.stream().map(OrderedProductInfoPojo::getProductId).collect(Collectors.toList()));
			ResponseEntity<ProductsResponse> orderedProducts = productServiceClient
					.validateOrderedProducts(orderedProductsPojo);
			ProductsResponse body = orderedProducts.getBody();
			if (body != null) {
				if (!body.getData().isEmpty()) {
					return body.getData();
				} else {
					throw new ProductNotFoundException("");
				}
			} else {
				throw new ProductNotFoundException("");
			}
		} catch (FeignException e) {
			throw e;
		} catch (Exception e) {
			throw new ProductNotFoundException("");
		}
	}

	private void validateProductDetails(List<MasterProductPojo> productInfo, String supplierId,
			OrderInfoPojo orderInfoPojo) {

		// validate supplier's products
		boolean allMatch = productInfo.stream().map(MasterProductPojo::getSupplierId)
				.allMatch(e -> e.equals(supplierId));

		if (allMatch) {

			productInfo.forEach(e ->

			orderInfoPojo.getProductInfos().forEach(f -> {

				List<ProductVariationPojo> collect = e.getProductVariations().stream()
						.filter(x -> x.getProductVariationId().equals(f.getProductId())).collect(Collectors.toList());

				// fdr available
				if (f.getModeOfOrder() == EModeOfOrder.LAST_MILE_FDR
						&& collect.get(0).getSalePriceWithLogistics() == null) {
					throw new ProductNotFoundException("Free Delivery And Returns Not Available For The Product");
				}

				// rto available
				if (!collect.get(0).isRtoAccepted() && f.isReturnOrder()) {
					throw new ProductNotFoundException("This Product Doesnot Have Return Policy");
				}

				// choose forward shipment in Actual cost
				if (f.getModeOfOrder() == EModeOfOrder.LAST_MILE_AC && !f.isForwardOrder()) {
					throw new ProductNotFoundException("Choose The Forward Order Delivery Option");
				}

				// stock available
				if (collect.get(0).getStockQty().compareTo(BigDecimal.valueOf(f.getQuantity())) < 0) {
					throw new ProductNotFoundException("Product Not In Stock");
				}

			}));

		} else {
			throw new ProductNotFoundException("Invalid Product Of The Store");
		}
	}

	private void validateSupplier(SupplierRegistration supplierInfo, OrderInfoPojo orderInfoPojo) {

		if (!supplierInfo.getSupplierStoreInfo().isStoreActive()) {
			throw new SupplierNotFoundException("Supplier Store Is Inactive");
		}

		ApplyCouponPojo couponPojo = orderInfoPojo.getCouponPojo();
		if (couponPojo != null) {
			List<SupplierStoreCoupon> supplierStoreCoupons = supplierInfo.getSupplierStoreInfo()
					.getSupplierStoreCoupons();

			if (!supplierStoreCoupons.isEmpty()) {
				List<SupplierStoreCoupon> list = supplierStoreCoupons.stream()
						.filter(e -> (e.getStoreCouponCode().equals(couponPojo.getCouponCode())
								&& Objects.equals(e.getStoreCouponId(), couponPojo.getCouponId())
								&& LocalDate.now().isBefore(e.getExpirationDate())))
						.collect(Collectors.toList());

				if (list.isEmpty()) {
					throw new InvalidCouponException("Coupon Not Applicable");
				} else {
					SupplierStoreCoupon storeCoupon = list.get(0);
//					if(storeCoupon.getDiscountType())
				}
			}
		}
	}

	private CustomerRegistration findCustomer(String customerId) {
		try {
			ResponseEntity<CustomerRegistrationResponse> customer = userServiceClient.findCustomerById(customerId);
			CustomerRegistrationResponse body = customer.getBody();
			if (body != null) {
				if (body.getData() != null) {
					return body.getData();
				} else {
					throw new CustomerNotFoundException("Customer Not Found");
				}
			} else {
				throw new CustomerNotFoundException("Customer Not Found");
			}
		} catch (FeignException e) {
			throw e;
		} catch (Exception e) {
			throw new CustomerNotFoundException("Customer Not Found");
		}
	}

	private void validateCustomerDetails(CustomerRegistration customerRegistration, OrderInfoPojo orderInfoPojo) {
		List<String> collect = customerRegistration.getProfiles().stream().map(UserProfile::getProfileId)
				.collect(Collectors.toList());
		if (!collect.contains(orderInfoPojo.getProfileId())) {
			throw new UserProfileNotFoundException("User Profile Not Found");
		}

		List<Long> address = customerRegistration.getUserAddressDetails().stream().map(UserAddressDetails::getAddressId)
				.collect(Collectors.toList());
		if ((!address.contains(orderInfoPojo.getShippingAddressId()))
				|| (!address.contains(orderInfoPojo.getBillingAddressId()))) {
			throw new AddressNotFoundException("Customer Address Not Found");
		}

	}

	private SupplierRegistration findSupplierInfo(String supplierId) {
		try {
			ResponseEntity<SupplierRegistartionGetResponse> supplierById = userServiceClient.getSupplierById(supplierId,
					ESupplierStatus.APPROVED);
			SupplierRegistartionGetResponse body = supplierById.getBody();
			if (body != null) {
				if (body.getData() != null) {
					return body.getData();
				} else {
					throw new SupplierNotFoundException("Supplier Not Found");
				}
			} else {
				throw new SupplierNotFoundException("Supplier Not Found");
			}
		} catch (FeignException e) {
			throw e;
		} catch (Exception e) {
			throw new SupplierNotFoundException("Supplier Not Found");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OrderInfo> getOrders(ECommission commissionType, EOrderInfoFilter filterType, String keyword,
			String supplierId, String orderStatus, int pageNumber, int pageSize) {
		try {
			List<OrderInfo> result = new ArrayList<>();
			List<OrderInfo> orderInfos = new ArrayList<>();
			if (filterType == null) {
				orderInfos.addAll(orderInfoRepository.findByProductOwnerIdAndOrderStatus(supplierId, orderStatus));
			} else if (filterType == EOrderInfoFilter.SKU_ID) {
				orderInfos.addAll(orderInfoRepository.findByProductOwnerIdAndOrderStatusAndSkuIdIgnoreCaseContaining(
						supplierId, orderStatus, keyword));
			} else if (filterType == EOrderInfoFilter.MODE_OF_ORDER) {
				orderInfos.addAll(
						orderInfoRepository.findByProductOwnerIdAndOrderStatusAndModeOfOrderIgnoreCaseContaining(
								supplierId, orderStatus, keyword));
			}

			if (!orderInfos.isEmpty() && commissionType != null) {

				Set<String> set = orderInfos.stream().map(OrderInfo::getProductId).collect(Collectors.toSet());
				List<String> collect = new ArrayList<>();
				collect.addAll(set);
				OrdersRequestPojo ordersRequestPojo = new OrdersRequestPojo(collect, commissionType.name());
				ResponseEntity<SuccessResponse> response = productServiceClient.getProductIds(ordersRequestPojo);
				List<String> productIds = new ArrayList<>();
				SuccessResponse body = response.getBody();
				if (body != null) {
					productIds.addAll((List<String>) body.getData());
				}
				productIds.forEach(e -> result.addAll(
						orderInfos.stream().filter(f -> f.getProductId().equals(e)).collect(Collectors.toList())));
			}

			return (List<OrderInfo>) PaginatedResponse.getPaginatedResponse(result, pageNumber, pageSize);

		} catch (Exception e) {
			throw new OrderException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public OrderInfo updateSerialNo(SerialNoUpdatePojo serialNoUpdatePojo) {

		try {
			Optional<OrderInfo> findById = orderInfoRepository.findById(serialNoUpdatePojo.getOrderId());
			if (findById.isPresent()) {
				OrderInfo orderInfo = findById.get();
				if (serialNoUpdatePojo.getSerialNos().size() == orderInfo.getOrderQuantityInfo().size()) {
					List<OrderQuantityInfo> orderQuantityInfo = orderInfo.getOrderQuantityInfo();
					List<String> serialNos = serialNoUpdatePojo.getSerialNos();
					for (int i = 0; i < orderQuantityInfo.size(); i++) {
						orderQuantityInfo.get(i).setSerialNumber(serialNos.get(i));
					}
					orderInfo.setOrderQuantityInfo(orderQuantityInfo);
					return orderInfoRepository.save(orderInfo);
				} else {
					throw new OrderException(PLEASE_PROVIDE + orderInfo.getOrderQuantityInfo().size() + SERIAL_NO);
				}
			} else {
				throw new OrderException(INVALID_ORDERID);
			}
		} catch (OrderException e) {
			throw e;
		} catch (Exception e) {
			throw new OrderException(SOMETHING_WENT_WRONG);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OrderInfo> getOrderByStatus(String supplierId, String orderStatus, int pageNumber, int pageSize,
			String modeOfOrder) {
		try {
			List<OrderInfo> list;
			if (modeOfOrder == null) {
				list = orderInfoRepository.findByProductOwnerIdAndOrderStatus(supplierId, orderStatus);
			} else {
				list = orderInfoRepository.findByProductOwnerIdAndOrderStatusAndModeOfOrderIgnoreCaseContaining(
						supplierId, orderStatus, modeOfOrder);
			}
			return (List<OrderInfo>) PaginatedResponse.getPaginatedResponse(list, pageNumber, pageSize);
		} catch (Exception e) {
			throw new OrderException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public OrderInfo rejectOrder(String orderId) {
		try {
			OrderInfo orderInfo = orderInfoRepository.findByOrderIdAndOrderStatus(orderId,
					EOrderStatus.INITIATED.name());
			if (orderInfo != null) {
				productServiceClient.increaseStockQuantity(
						new IncreaseStockQuantityPojo(orderInfo.getProductId(), orderInfo.getOrderQuantity()));
				orderInfo.setOrderStatus(EOrderStatus.REJECTED.name());

				// Refund And Push notification is pending

				return orderInfoRepository.save(orderInfo);
			} else {
				throw new OrderException(INVALID_ORDERID);
			}
		} catch (OrderException | FeignException e) {
			throw e;
		} catch (Exception e) {
			throw new OrderException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<OrderInfo> confirmOrder(ConfirmOrderPojo confirmOrderPojo) {
		List<OrderInfo> findAllById = orderInfoRepository.findAllById(confirmOrderPojo.getOrderIds());
		if (findAllById.size() == confirmOrderPojo.getOrderIds().size()) {
			String modeOfOrder = findAllById.get(0).getModeOfOrder();
			boolean allMatch = findAllById.stream().allMatch(e -> e.getModeOfOrder().equals(modeOfOrder));
			if (allMatch) {
				if (modeOfOrder.equals(EModeOfOrder.SUPPLIER_SHIPMENT.name())) {
					return setSupplierShipmentDetails(findAllById, confirmOrderPojo);
				} else if (modeOfOrder.equals(EModeOfOrder.LAST_MILE_AC.name())
						|| modeOfOrder.equals(EModeOfOrder.LAST_MILE_FDR.name())) {
					return setLastMileDetails(findAllById, confirmOrderPojo);
				} else {
					findAllById.forEach(e -> e.setOrderStatus(EOrderStatus.CONFIRMED.name()));
					return orderInfoRepository.saveAll(findAllById);
				}
			} else {
				throw new OrderException(SAME_MODE_ERROR);
			}
		} else {
			throw new OrderException(INVALID_ORDERID);
		}
	}

	private List<OrderInfo> setSupplierShipmentDetails(List<OrderInfo> findAllById, ConfirmOrderPojo confirmOrderPojo) {
		if (findAllById.size() == 1) {
			OrderInfo orderInfo = findAllById.get(0);
			List<LogisticInfo> logisticInfos = new ArrayList<>();
			LogisticInfo logisticInfo = new LogisticInfo();
			logisticInfo.setLogisticPartnerName(confirmOrderPojo.getLogisticPartnerName());
			logisticInfo.setLogisticUrl(confirmOrderPojo.getLogisticUrl());
			logisticInfo.setTrackingId(confirmOrderPojo.getTrackingId());
			logisticInfos.add(logisticInfo);
			orderInfo.setLogisticInfo(logisticInfos);
			orderInfo.setOrderStatus(EOrderStatus.CONFIRMED.name());
			OrderInfo save = orderInfoRepository.save(orderInfo);
			List<OrderInfo> infos = new ArrayList<>();
			infos.add(save);
			return infos;
		} else {
			throw new OrderException(SUPPLIER_SHIPMENT_ERROR);
		}
	}

	private List<OrderInfo> setLastMileDetails(List<OrderInfo> findAllById, ConfirmOrderPojo confirmOrderPojo) {
		findAllById.forEach(e -> {
			// fetch logistic details third party integration
			e.setOrderStatus(EOrderStatus.CONFIRMED.name());
			e.setPickupAddressId(confirmOrderPojo.getPickupAddressId());
			e.setReturnAddressId(confirmOrderPojo.getPickupAddressId());
		});
		return orderInfoRepository.saveAll(findAllById);
	}

}
