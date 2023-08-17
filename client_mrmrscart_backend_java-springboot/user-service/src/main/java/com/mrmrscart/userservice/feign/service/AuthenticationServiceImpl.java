package com.mrmrscart.userservice.feign.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.mrmrscart.userservice.common.admin.AdminConstant.*;
import com.mrmrscart.userservice.entity.admin.AdminRegistration;
import com.mrmrscart.userservice.entity.admin.EAdminStatus;
import com.mrmrscart.userservice.entity.customer.CustomerRegistration;
import com.mrmrscart.userservice.entity.reseller.ResellerRegistration;
import com.mrmrscart.userservice.entity.supplier.ESupplierStatus;
import com.mrmrscart.userservice.entity.supplier.EUserRole;
import com.mrmrscart.userservice.entity.supplier.StaffManagementInfo;
import com.mrmrscart.userservice.entity.supplier.SupplierRegistration;
import com.mrmrscart.userservice.exception.supplier.UserIdNotFoundException;
import com.mrmrscart.userservice.feign.pojo.AuthenticationPojo;
import com.mrmrscart.userservice.feign.pojo.JwtInfoPojo;
import com.mrmrscart.userservice.repository.admin.AdminRegistrationRepository;
import com.mrmrscart.userservice.repository.customer.CustomerRegistrationRepository;
import com.mrmrscart.userservice.repository.reseller.ResellerRegistrationRepository;
import com.mrmrscart.userservice.repository.supplier.StaffManagementRepository;
import com.mrmrscart.userservice.repository.supplier.SupplierRegistrationRepository;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	private AdminRegistrationRepository adminRegistrationRepository;

	@Autowired
	private SupplierRegistrationRepository supplierRegistrationRepository;

	@Autowired
	private StaffManagementRepository staffManagementRepository;

	@Autowired
	private ResellerRegistrationRepository resellerRegistrationRepository;

	@Autowired
	private CustomerRegistrationRepository customerRegistrationRepository;

	@Override
	public JwtInfoPojo verifyUser(AuthenticationPojo authenticationPojo) {
		JwtInfoPojo jwtInfoPojo = new JwtInfoPojo();
		if (authenticationPojo.getUserName().contains("@")) {
			if (authenticationPojo.getUserType() == EUserRole.ADMIN) {
				AdminRegistration findByEmailIdAndStatus = adminRegistrationRepository
						.findByEmailIdAndStatus(authenticationPojo.getUserName(), EAdminStatus.APPROVED);
				if (findByEmailIdAndStatus != null) {
					jwtInfoPojo.setEmailId(findByEmailIdAndStatus.getEmailId());
					jwtInfoPojo.setUserId(findByEmailIdAndStatus.getAdminRegistrationId());
				}
			} else if (authenticationPojo.getUserType() == EUserRole.SUPPLIER) {
				SupplierRegistration findByEmailIdAndStatus = supplierRegistrationRepository
						.findByEmailIdAndStatus(authenticationPojo.getUserName(), ESupplierStatus.APPROVED);
				if (findByEmailIdAndStatus != null) {
					jwtInfoPojo.setEmailId(findByEmailIdAndStatus.getEmailId());
					jwtInfoPojo.setUserId(findByEmailIdAndStatus.getSupplierId());
				} else {
					StaffManagementInfo findByEmailIdAndIsDelete = staffManagementRepository
							.findByEmailIdAndIsDelete(authenticationPojo.getUserName(), false);
					if (findByEmailIdAndIsDelete != null) {
						jwtInfoPojo.setEmailId(findByEmailIdAndIsDelete.getEmailId());
						jwtInfoPojo.setUserId(findByEmailIdAndIsDelete.getStaffId());
					}
				}
			} else if (authenticationPojo.getUserType() == EUserRole.RESELLER) {
				ResellerRegistration findByEmailIdAndIsDisabled = resellerRegistrationRepository
						.findByEmailIdAndIsDisabled(authenticationPojo.getUserName(), false);
				if (findByEmailIdAndIsDisabled != null) {
					jwtInfoPojo.setEmailId(findByEmailIdAndIsDisabled.getEmailId());
					jwtInfoPojo.setUserId(findByEmailIdAndIsDisabled.getResellerId());
				}

			} else if (authenticationPojo.getUserType() == EUserRole.CUSTOMER) {
				CustomerRegistration findByEmailIdAndIsDisabled = customerRegistrationRepository
						.findByEmailIdAndIsDisabled(authenticationPojo.getUserName(), false);
				if (findByEmailIdAndIsDisabled != null) {
					jwtInfoPojo.setEmailId(findByEmailIdAndIsDisabled.getEmailId());
					jwtInfoPojo.setUserId(findByEmailIdAndIsDisabled.getCustomerId());
				}
			}
		} else {
			if (authenticationPojo.getUserType() == EUserRole.ADMIN) {
				AdminRegistration findByMobileNumberAndStatus = adminRegistrationRepository
						.findByMobileNumberAndStatus(authenticationPojo.getUserName(), EAdminStatus.APPROVED);
				if (findByMobileNumberAndStatus != null) {
					jwtInfoPojo.setEmailId(findByMobileNumberAndStatus.getEmailId());
					jwtInfoPojo.setUserId(findByMobileNumberAndStatus.getAdminRegistrationId());
				}
			} else if (authenticationPojo.getUserType() == EUserRole.SUPPLIER) {
				SupplierRegistration findByMobileNumberAndStatus = supplierRegistrationRepository
						.findByMobileNumberAndStatus(authenticationPojo.getUserName(), ESupplierStatus.APPROVED);
				if (findByMobileNumberAndStatus != null) {
					jwtInfoPojo.setEmailId(findByMobileNumberAndStatus.getEmailId());
					jwtInfoPojo.setUserId(findByMobileNumberAndStatus.getSupplierId());
				} else {
					StaffManagementInfo findByMobileNumberAndIsDelete = staffManagementRepository
							.findByMobileNumberAndIsDeleteAndIsMobileNumberVerified(authenticationPojo.getUserName(),
									false, true);
					if (findByMobileNumberAndIsDelete != null) {
						jwtInfoPojo.setEmailId(findByMobileNumberAndIsDelete.getEmailId());
						jwtInfoPojo.setUserId(findByMobileNumberAndIsDelete.getStaffId());
					}
				}
			} else if (authenticationPojo.getUserType() == EUserRole.RESELLER) {
				ResellerRegistration findByMobileNumberAndIsDisabled = resellerRegistrationRepository
						.findByMobileNumberAndIsDisabled(authenticationPojo.getUserName(), false);
				if (findByMobileNumberAndIsDisabled != null) {
					jwtInfoPojo.setEmailId(findByMobileNumberAndIsDisabled.getEmailId());
					jwtInfoPojo.setUserId(findByMobileNumberAndIsDisabled.getResellerId());
				}

			} else if (authenticationPojo.getUserType() == EUserRole.CUSTOMER) {
				CustomerRegistration findByMobileNumberAndIsDisabled = customerRegistrationRepository
						.findByMobileNumberAndIsDisabled(authenticationPojo.getUserName(), false);
				if (findByMobileNumberAndIsDisabled != null) {
					jwtInfoPojo.setEmailId(findByMobileNumberAndIsDisabled.getEmailId());
					jwtInfoPojo.setUserId(findByMobileNumberAndIsDisabled.getCustomerId());
				}
			}
		}
		return jwtInfoPojo;
	}

	@Override
	public StaffManagementInfo getStaffManagementInfo(String emailId) {
		return staffManagementRepository.findByEmailIdAndIsDelete(emailId, false);
	}

	@Override
	public AdminRegistration getAdminInfo(String emailId) {
		try {
			return adminRegistrationRepository.findByEmailIdAndStatusAndIsDelete(emailId, EAdminStatus.APPROVED, false);
		} catch (Exception e) {
			throw new UserIdNotFoundException(SOMETHING_WENT_WRONG);
		}
	}

}
