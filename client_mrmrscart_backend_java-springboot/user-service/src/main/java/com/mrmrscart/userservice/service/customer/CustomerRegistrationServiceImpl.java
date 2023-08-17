package com.mrmrscart.userservice.service.customer;

import static com.mrmrscart.userservice.common.customer.CustomerConstant.ACCOUNT_EXIST;
import static com.mrmrscart.userservice.common.customer.CustomerConstant.C;
import static com.mrmrscart.userservice.common.customer.CustomerConstant.EMAIL_EXIST;
import static com.mrmrscart.userservice.common.customer.CustomerConstant.INVALID_STORE_CODE;
import static com.mrmrscart.userservice.common.customer.CustomerConstant.SOMETHING_WENT_WRONG;
import static com.mrmrscart.userservice.common.customer.CustomerConstant.WISH_CONTINUE;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mrmrscart.userservice.entity.customer.CustomerRegistration;
import com.mrmrscart.userservice.entity.customer.CustomerStores;
import com.mrmrscart.userservice.entity.customer.UserProfile;
import com.mrmrscart.userservice.entity.supplier.EUserRole;
import com.mrmrscart.userservice.entity.supplier.SupplierStoreInfo;
import com.mrmrscart.userservice.exception.customer.CustomerException;
import com.mrmrscart.userservice.exception.customer.CustomerNotFoundException;
import com.mrmrscart.userservice.feign.client.AuthServiceClient;
import com.mrmrscart.userservice.feign.pojo.CustomerPojo;
import com.mrmrscart.userservice.pojo.customer.CustomerRegistrationPojo;
import com.mrmrscart.userservice.pojo.customer.DropDownPojo;
import com.mrmrscart.userservice.pojo.supplier.UserLoginPojo;
import com.mrmrscart.userservice.pojo.supplier.UserRole;
import com.mrmrscart.userservice.repository.customer.CustomerRegistrationRepository;
import com.mrmrscart.userservice.repository.supplier.SupplierStoreInfoRepository;
import com.mrmrscart.userservice.response.supplier.UserLoginResponse;

import feign.FeignException;

@Service
public class CustomerRegistrationServiceImpl implements CustomerRegistrationService {

	@Autowired
	private CustomerRegistrationRepository customerRegistrationRepository;

	@Autowired
	private SupplierStoreInfoRepository supplierStoreInfoRepository;

	@Autowired
	private AuthServiceClient authServiceClient;

	@Override
	public List<DropDownPojo> getCustomers(List<String> customerId) {
		List<DropDownPojo> downPojos = new ArrayList<>();
		List<CustomerRegistration> customers = customerRegistrationRepository.findAllById(customerId);
		if (!customers.isEmpty()) {
			customers.forEach(customer -> {
				DropDownPojo pojo = new DropDownPojo(customer.getCustomerId(), customer.getCustomerName());
				downPojos.add(pojo);
			});

		}
		return downPojos;
	}

	@Override
	@Transactional
	public CustomerRegistration addCustomer(CustomerRegistrationPojo customerRegistrationPojo) {
		try {
			SupplierStoreInfo supplierStoreInfo = supplierStoreInfoRepository
					.findBySupplierStoreCodeAndIsStoreActive(customerRegistrationPojo.getStoreCode(), true);
			if (supplierStoreInfo == null) {
				throw new CustomerException(INVALID_STORE_CODE);
			}
			if (!customerRegistrationPojo.isWished()) {
				ResponseEntity<UserLoginResponse> userByEmailId = authServiceClient
						.getUserByEmailId(customerRegistrationPojo.getEmailId());
				UserLoginPojo data = userByEmailId.getBody().getData();
				if (data != null) {
					List<UserRole> userRoles = data.getUserRoles();
					if (userRoles.stream().filter(e -> e.getRole() == EUserRole.CUSTOMER).count() > 0) {
						throw new CustomerException(EMAIL_EXIST);
					} else if (!userRoles.isEmpty()) {
						StringBuilder stringBuilder = new StringBuilder();
						userRoles.forEach(e -> stringBuilder.append(e.getRole() + ", "));
						throw new CustomerException(ACCOUNT_EXIST + stringBuilder + WISH_CONTINUE);
					} else {

						saveUserLogin(customerRegistrationPojo);
						return saveCustomer(customerRegistrationPojo, supplierStoreInfo);
					}
				} else {
					saveUserLogin(customerRegistrationPojo);
					return saveCustomer(customerRegistrationPojo, supplierStoreInfo);
				}
			} else {
				saveUserLogin(customerRegistrationPojo);
				return saveCustomer(customerRegistrationPojo, supplierStoreInfo);
			}
		} catch (CustomerException | FeignException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomerException(SOMETHING_WENT_WRONG);
		}
	}

	private CustomerRegistration saveCustomer(CustomerRegistrationPojo customerRegistrationPojo,
			SupplierStoreInfo supplierStoreInfo) {
		CustomerRegistration customerRegistration = new CustomerRegistration();
		BeanUtils.copyProperties(customerRegistrationPojo, customerRegistration);
		customerRegistration.setRegisteredAt(LocalDateTime.now());
		String customerCode = getCustomerCode();
		customerRegistration.setCustomerCode(customerCode);
		CustomerStores customerStores = new CustomerStores();
		customerStores.setStoreCode(supplierStoreInfo.getSupplierStoreCode());
		customerStores.setStoreType(EUserRole.SUPPLIER);
		customerStores.setJoinedAt(LocalDateTime.now());

		List<UserProfile> profiles = new ArrayList<>();
		profiles.add(getUserProfile(customerRegistrationPojo, customerCode));
		customerRegistration.setProfiles(profiles);
		CustomerRegistration save = customerRegistrationRepository.save(customerRegistration);
		List<CustomerStores> stores = new ArrayList<>();
		stores.add(customerStores);
		save.setCustomerStores(stores);
		return customerRegistrationRepository.save(save);

	}

	private void saveUserLogin(CustomerRegistrationPojo customerRegistrationPojo) {
		CustomerPojo customerPojo = new CustomerPojo(customerRegistrationPojo.getEmailId(),
				customerRegistrationPojo.getPassword());
		authServiceClient.addCustomer(customerPojo);
	}

	private String getCustomerCode() {
		int size = customerRegistrationRepository.findAll().size();
		if (size > 0) {
			return C + String.format("%02d", size);
		} else {
			return C + "01";
		}
	}

	private UserProfile getUserProfile(CustomerRegistrationPojo customerRegistrationPojo, String customerCode) {
		UserProfile userProfile = new UserProfile();
		userProfile.setProfileName(customerRegistrationPojo.getCustomerName());
		userProfile.setProfilePrimary(true);
		userProfile.setCreatedAt(LocalDateTime.now());
		LocalDate localDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddYYY");
		String formattedString = localDate.format(formatter);
		String substring = customerCode.substring(1);
		int parseInt = Integer.parseInt(substring);
		userProfile.setProfileId("PRF" + customerCode + formattedString + String.format("%02d", parseInt));
		return userProfile;
	}

	@Override
	public CustomerRegistration findCustomerById(String customerId) {
		try {
			CustomerRegistration customerRegistration = customerRegistrationRepository
					.findByCustomerIdAndIsDisabled(customerId, false);
			if (customerRegistration != null) {
				return customerRegistration;
			} else {
				throw new CustomerNotFoundException("Customer Not Found");
			}
		} catch (CustomerNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new CustomerException(SOMETHING_WENT_WRONG);

		}
	}

}
