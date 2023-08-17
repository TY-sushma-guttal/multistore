package com.mrmrscart.userservice.service.supplier;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

import com.mrmrscart.userservice.entity.supplier.ESupplierStatus;
import com.mrmrscart.userservice.entity.supplier.EUserRole;
import com.mrmrscart.userservice.entity.supplier.SupplierRegistration;
import com.mrmrscart.userservice.entity.supplier.UserAddressDetails;
import com.mrmrscart.userservice.feign.pojo.SupplierChangesHistoryPojo;
import com.mrmrscart.userservice.feign.response.AuthenticateResponse;
import com.mrmrscart.userservice.pojo.supplier.ChangePasswordPojo;
import com.mrmrscart.userservice.pojo.supplier.ForgotPasswordPojo;
import com.mrmrscart.userservice.pojo.supplier.ReferredSupplierPojo;
import com.mrmrscart.userservice.pojo.supplier.SupplierRegistrationPojo;
import com.mrmrscart.userservice.pojo.supplier.UserAddressDetailsPojo;
import com.mrmrscart.userservice.pojo.supplier.UserLoginPojo;
import com.mrmrscart.userservice.wrapper.supplier.SupplierInfoWrapper;

public interface SupplierService {

//	public UserLogin forgotPassword(ForgotPasswordPojo forgotPasswordPojo);

	public UserAddressDetails addAddressDetails(UserAddressDetailsPojo addressDetailsPojo);

	public UserAddressDetails updateAddressDetails(UserAddressDetailsPojo userAddressDetailsPojo);

	public List<UserAddressDetails> getAllAddresses(String supplierID);

	public UserAddressDetails changePrimaryAddress(String supplierId, Long addressId);

	public String deleteAddress(String supplierId, Long addressId);

	public UserAddressDetails getAddressById(String supplierId, Long addressId);

	public UserLoginPojo forgotPassword(ForgotPasswordPojo forgotPasswordPojo);

	public String sendOtpRegistration(String mobileNumber, EUserRole userType) throws SendFailedException;

	public String sendOtpForForgotPassword(String userName, EUserRole userType)
			throws UnsupportedEncodingException, MessagingException;

	public String verifyOtp(String userName, int otp);

	public SupplierRegistration addSupplier(SupplierRegistrationPojo supplierRegistrationPojo)
			throws UnsupportedEncodingException, MessagingException;

	public AuthenticateResponse verifyloginWithOtp(String userName, int otp, EUserRole userType);

	public String changePassword(ChangePasswordPojo changePasswordPojo);

	public SupplierRegistration updateProfile(SupplierRegistrationPojo supplierRegistrationPojo);

	public SupplierRegistration updateSupplierProfile(SupplierChangesHistoryPojo supplierChangesHistoryPojo);

	public boolean approveSupplierProfile(SupplierChangesHistoryPojo supplierChangesHistoryPojo);

	public SupplierRegistration getSupplierById(String id, ESupplierStatus status);

	public ReferredSupplierPojo getReferredSupplier(String supplierId);

	public SupplierInfoWrapper findSupplierAndStoreInfo(String supplierId, ESupplierStatus status);

	public SupplierRegistration getSupplierById(String id);
}
