package com.mrmrscart.userservice.service.supplier;

import static com.mrmrscart.userservice.common.supplier.SupplierConstant.INVALID_SUPPLIER;
import static com.mrmrscart.userservice.common.supplier.SupplierConstant.SOMETHING_WENT_WRONG;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mrmrscart.userservice.entity.customer.CustomerRegistration;
import com.mrmrscart.userservice.entity.supplier.ESupplierStatus;
import com.mrmrscart.userservice.entity.supplier.SupplierRegistration;
import com.mrmrscart.userservice.exception.supplier.SupplierException;
import com.mrmrscart.userservice.feign.client.OrdersPaymentsServiceClient;
import com.mrmrscart.userservice.feign.client.ProductCategoryClient;
import com.mrmrscart.userservice.feign.pojo.OrderPojo;
import com.mrmrscart.userservice.feign.response.OrderResponse;
import com.mrmrscart.userservice.feign.response.ProductVariationGetResponse;
import com.mrmrscart.userservice.pojo.supplier.OrderResponsePojo;
import com.mrmrscart.userservice.pojo.supplier.RevenueSalesTotalCountPojo;
import com.mrmrscart.userservice.repository.customer.CustomerRegistrationRepository;
import com.mrmrscart.userservice.repository.customer.CustomerStoresRepository;
import com.mrmrscart.userservice.repository.supplier.SupplierRegistrationRepository;
import com.mrmrscart.userservice.response.supplier.SuccessResponse;

import feign.FeignException;

@Service
public class SupplierReportServiceImpl implements SupplierReportService {

	@Autowired
	private SupplierRegistrationRepository supplierRegistrationRepository;

	@Autowired
	private CustomerStoresRepository customerStoresRepository;

	@Autowired
	private OrdersPaymentsServiceClient ordersPaymentsServiceClient;

	@Autowired
	private ProductCategoryClient productCategoryClient;

	@Autowired
	private CustomerRegistrationRepository customerRegistrationRepository;

	@Override
	public RevenueSalesTotalCountPojo getRevenueSalesTotalCount(String supplierId) {
		try {
			RevenueSalesTotalCountPojo revenueSalesTotalCountPojo = new RevenueSalesTotalCountPojo();
			SupplierRegistration findBySupplierIdAndStatus = supplierRegistrationRepository
					.findBySupplierIdAndStatus(supplierId, ESupplierStatus.APPROVED);
			if (findBySupplierIdAndStatus != null) {
				revenueSalesTotalCountPojo.setTotalFreeOrders(findBySupplierIdAndStatus.getTotalFreeOrderCount()
						- findBySupplierIdAndStatus.getSignupFreeOrderCount());
				revenueSalesTotalCountPojo.setTotalReferrals(supplierRegistrationRepository
						.countByReferredByIdAndSignupFreeOrderCountGreaterThan(supplierId, 0l));
				revenueSalesTotalCountPojo.setTotalCustomers(customerStoresRepository
						.countByStoreCode(findBySupplierIdAndStatus.getSupplierStoreInfo().getSupplierStoreCode()));
				ResponseEntity<SuccessResponse> totalOrdersCount = ordersPaymentsServiceClient
						.getTotalOrdersCount(supplierId);
				if (totalOrdersCount.getBody() != null) {
					SuccessResponse body = totalOrdersCount.getBody();
					if (body != null) {
						revenueSalesTotalCountPojo.setTotalOrders((int) body.getData());
					}
				}
				ResponseEntity<SuccessResponse> sumOfOrderAmount = ordersPaymentsServiceClient.getSumOfOrderAmount(supplierId);
				if(sumOfOrderAmount.getBody()!=null) {
					SuccessResponse body = sumOfOrderAmount.getBody();
					if(body!=null) {
						revenueSalesTotalCountPojo.setTotalSalesValue((int)body.getData());
					}
				}
			} else {
				throw new SupplierException(INVALID_SUPPLIER);
			}
			return revenueSalesTotalCountPojo;
		} catch (FeignException | SupplierException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SupplierException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<Long> getRevenueSalesReferrals(int year, String supplierId) {
		try {
			List<Long> referrals = new ArrayList<>();
			for (int i = 1; i <= 12; i++) {
				referrals.add((long) supplierRegistrationRepository.findByYearMonth(supplierId, 0l, year, i).size());
			}
			return referrals;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SupplierException(SOMETHING_WENT_WRONG);
		}

	}

	@Override
	public List<Long> getRevenueSalesCustomers(int year, String storeCode) {
		try {
			List<Long> customers = new ArrayList<>();
			for (int i = 1; i <= 12; i++) {
				customers.add((long) customerStoresRepository.findByYearMonth(storeCode, year, i).size());
			}
			return customers;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SupplierException(SOMETHING_WENT_WRONG);
		}

	}

	@Override
	public ResponseEntity<SuccessResponse> getOrdersTotalCount(String supplierId) {
		try {
			return ordersPaymentsServiceClient.getOrdersTotalCount(supplierId);
		} catch (FeignException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SupplierException(SOMETHING_WENT_WRONG);
		}

	}

	@Override
	public ResponseEntity<SuccessResponse> getOrdersMonthInfo(int year, String supplierId) {
		try {
			return ordersPaymentsServiceClient.getOrdersMonthInfo(year, supplierId);
		} catch (FeignException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SupplierException(SOMETHING_WENT_WRONG);
		}

	}

	@Override
	public List<OrderResponsePojo> getOrdersSummary(int year, String supplierId, int pageNumber, int pageSize,
			String status) {

		try {
			ResponseEntity<OrderResponse> ordersSummary = ordersPaymentsServiceClient.getOrdersSummary(year,
					supplierId, pageNumber, pageSize, status);
			if (ordersSummary != null) {
				OrderResponse body = ordersSummary.getBody();
				if (body != null) {
					List<OrderPojo> orderPojos = body.getData();
					List<OrderResponsePojo> orderResponsePojos = new ArrayList<>();
					orderPojos.forEach(e -> 

						orderResponsePojos.add(getOrderResponse(e))
					);
					return orderResponsePojos;
				}
			}
		} catch (FeignException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SupplierException(SOMETHING_WENT_WRONG);
		}
		return new ArrayList<>();
	}

	private OrderResponsePojo getOrderResponse(OrderPojo e) {
		OrderResponsePojo orderResponsePojo = new OrderResponsePojo();
		BeanUtils.copyProperties(e, orderResponsePojo);
		ResponseEntity<ProductVariationGetResponse> productVariationWithId = productCategoryClient
				.getProductVariationWithId(e.getProductId());
		if (productVariationWithId != null) {
			ProductVariationGetResponse body2 = productVariationWithId.getBody();
			if (body2 != null) {
				orderResponsePojo.setProductName(body2.getData().getProductTitle());
			}
		}
		Optional<CustomerRegistration> findById = customerRegistrationRepository.findById(e.getOrderedById());
		if (findById.isPresent()) {
			orderResponsePojo.setCustomerName(findById.get().getCustomerName());
		}
		return orderResponsePojo;
	}

	@Override
	public ResponseEntity<SuccessResponse> getSumOfOrderAmountMonthInfo(int year, String supplierId) {
		try {
			return ordersPaymentsServiceClient.getSumOfOrderAmountMonthInfo(year, supplierId);
		} catch (FeignException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SupplierException(SOMETHING_WENT_WRONG);
		}
	}

}
