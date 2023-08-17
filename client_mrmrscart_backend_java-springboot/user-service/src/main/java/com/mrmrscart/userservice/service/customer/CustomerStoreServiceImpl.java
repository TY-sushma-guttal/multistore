package com.mrmrscart.userservice.service.customer;

import static com.mrmrscart.userservice.common.customer.CustomerConstant.INVALID_STORE_CODE;
import static com.mrmrscart.userservice.common.supplier.SupplierConstant.INACTIVE_STORE;
import static com.mrmrscart.userservice.common.supplier.SupplierConstant.INVALID_SUPPLIER;
import static com.mrmrscart.userservice.common.supplier.SupplierConstant.SOMETHING_WENT_WRONG;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrmrscart.userservice.entity.customer.CustomerStores;
import com.mrmrscart.userservice.entity.supplier.ESupplierStatus;
import com.mrmrscart.userservice.entity.supplier.SupplierRegistration;
import com.mrmrscart.userservice.entity.supplier.SupplierStoreInfo;
import com.mrmrscart.userservice.exception.customer.CustomerException;
import com.mrmrscart.userservice.exception.supplier.InactiveSupplierStoreException;
import com.mrmrscart.userservice.exception.supplier.SupplierException;
import com.mrmrscart.userservice.exception.supplier.SupplierNotFoundException;
import com.mrmrscart.userservice.pojo.customer.DropDownPojo;
import com.mrmrscart.userservice.pojo.supplier.SupplierStoreInfoResponsePojo;
import com.mrmrscart.userservice.repository.customer.CustomerStoresRepository;
import com.mrmrscart.userservice.repository.supplier.SupplierRegistrationRepository;
import com.mrmrscart.userservice.repository.supplier.SupplierStoreInfoRepository;
import com.mrmrscart.userservice.wrapper.customer.CustomerWrapper;

@Service
public class CustomerStoreServiceImpl implements CustomerStoreService {

	@Autowired
	private SupplierRegistrationRepository supplierRegistrationRepository;

	@Autowired
	private CustomerStoresRepository customerStoresRepository;

	@Autowired
	private SupplierStoreInfoRepository supplierStoreInfoRepository;

	@Override
	public List<DropDownPojo> getFavouritreCustomers(String supplierId) {
		List<DropDownPojo> dropDownPojos = new ArrayList<>();
		SupplierRegistration supplier = supplierRegistrationRepository.findBySupplierIdAndStatus(supplierId,
				ESupplierStatus.APPROVED);
		if (supplier != null) {
			List<CustomerStores> customerStores = customerStoresRepository
					.findByStoreCode(supplier.getSupplierStoreInfo().getSupplierStoreCode()).stream()
					.filter(CustomerStores::isFavourite).collect(Collectors.toList());
			customerStores.forEach(f -> {
				DropDownPojo downPojo = new DropDownPojo(f.getCustomerRegistration().getCustomerId(),
						f.getCustomerRegistration().getCustomerName());
				dropDownPojos.add(downPojo);
			});

		} else {
			throw new SupplierException(INVALID_SUPPLIER);
		}
		return dropDownPojos;
	}

	@Override
	public List<CustomerWrapper> getCustomers(String supplierId) {

		try {
			SupplierRegistration supplierRegistration = supplierRegistrationRepository
					.findBySupplierIdAndStatus(supplierId, ESupplierStatus.APPROVED);
			if (supplierRegistration != null) {
				String storeCode = supplierRegistration.getSupplierStoreInfo().getSupplierStoreCode();
				if (supplierRegistration.getSupplierStoreInfo().isStoreActive()) {
					List<CustomerStores> list = customerStoresRepository.findByStoreCode(storeCode);
					List<CustomerWrapper> arrayList = new ArrayList<>();
					list.forEach(e -> {
						CustomerWrapper customerWrapper = new CustomerWrapper();
						BeanUtils.copyProperties(e.getCustomerRegistration(), customerWrapper);
						// set lastMonthPurchase, currentMonthPurchase, totalPurchase
						arrayList.add(customerWrapper);
					});
					return arrayList;
				} else {
					throw new InactiveSupplierStoreException(INACTIVE_STORE);
				}
			} else {
				throw new SupplierNotFoundException(INVALID_SUPPLIER);
			}
		} catch (SupplierNotFoundException | InactiveSupplierStoreException e) {
			throw e;
		} catch (Exception e) {
			throw new SupplierException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public SupplierStoreInfoResponsePojo validateStoreCode(String storeCode) {
		try {
			if (storeCode.substring(0, 3).equals("SUP")) {
				SupplierStoreInfo supplierStoreInfo = supplierStoreInfoRepository
						.findBySupplierStoreCodeAndIsStoreActive(storeCode, true);
				if (supplierStoreInfo != null) {
					SupplierRegistration findBySupplierStoreInfo = supplierRegistrationRepository
							.findBySupplierStoreInfo(supplierStoreInfo);
					if (findBySupplierStoreInfo != null) {
						SupplierStoreInfoResponsePojo supplierStoreInfoResponsePojo = new SupplierStoreInfoResponsePojo();
						BeanUtils.copyProperties(supplierStoreInfo, supplierStoreInfoResponsePojo);
						supplierStoreInfoResponsePojo.setStoreThemes(supplierStoreInfo.getStoreThemes());
						supplierStoreInfoResponsePojo
								.setSupplierStoreCoupons(supplierStoreInfo.getSupplierStoreCoupons());
						supplierStoreInfoResponsePojo.setSupplierId(findBySupplierStoreInfo.getSupplierId());
						return supplierStoreInfoResponsePojo;
					} else {
						throw new CustomerException(INVALID_STORE_CODE);
					}
				} else {
					throw new CustomerException(INVALID_STORE_CODE);
				}
			} else {
				throw new CustomerException("Please Provide Supplier Store Code ");
			}
		} catch (CustomerException e) {
			throw e;
		} catch (Exception e) {
			throw new SupplierException(SOMETHING_WENT_WRONG);
		}

	}

}
