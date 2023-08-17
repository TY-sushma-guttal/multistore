package com.mrmrscart.userservice.service.supplier;

import static com.mrmrscart.userservice.common.supplier.SupplierConstant.OTP_SUCCESS_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mock.web.MockHttpServletRequest;

import com.google.common.cache.LoadingCache;
import com.mrmrscart.userservice.audit.BaseConfigController;
import com.mrmrscart.userservice.entity.customer.CustomerRegistration;
import com.mrmrscart.userservice.entity.supplier.ESupplierStatus;
import com.mrmrscart.userservice.entity.supplier.EUserRole;
import com.mrmrscart.userservice.entity.supplier.StaffManagementInfo;
import com.mrmrscart.userservice.entity.supplier.SupplierRegistration;
import com.mrmrscart.userservice.entity.supplier.SupplierStoreInfo;
import com.mrmrscart.userservice.entity.supplier.UserAddressDetails;
import com.mrmrscart.userservice.feign.client.AuthServiceClient;
import com.mrmrscart.userservice.feign.client.NotificationReportLogService;
import com.mrmrscart.userservice.feign.pojo.AuthenticationOtpPojo;
import com.mrmrscart.userservice.feign.pojo.AuthenticationPojo;
import com.mrmrscart.userservice.feign.pojo.JwtInfoPojo;
import com.mrmrscart.userservice.feign.pojo.SupplierChangesHistoryPojo;
import com.mrmrscart.userservice.feign.response.AuthenticateOtpResponse;
import com.mrmrscart.userservice.feign.response.AuthenticateResponse;
import com.mrmrscart.userservice.feign.service.AuthenticationServiceImpl;
import com.mrmrscart.userservice.pojo.supplier.ChangePasswordPojo;
import com.mrmrscart.userservice.pojo.supplier.ForgotPasswordPojo;
import com.mrmrscart.userservice.pojo.supplier.ReferredSupplierPojo;
import com.mrmrscart.userservice.pojo.supplier.SupplierRegistrationPojo;
import com.mrmrscart.userservice.pojo.supplier.UserAddressDetailsPojo;
import com.mrmrscart.userservice.pojo.supplier.UserLoginPojo;
import com.mrmrscart.userservice.pojo.supplier.UserRole;
import com.mrmrscart.userservice.repository.customer.CustomerRegistrationRepository;
import com.mrmrscart.userservice.repository.supplier.StaffManagementRepository;
import com.mrmrscart.userservice.repository.supplier.SupplierRegistrationRepository;
import com.mrmrscart.userservice.repository.supplier.UserAddressDetailsRepository;
import com.mrmrscart.userservice.response.supplier.SuccessResponse;
import com.mrmrscart.userservice.response.supplier.UserLoginResponse;

@ExtendWith(MockitoExtension.class)
public class SupplierServiceImplTest {

	@InjectMocks
	private SupplierServiceImpl service;

	@InjectMocks
	private BaseConfigController baseConfigController;

	@Mock
	private UserAddressDetailsRepository userAddressDetailsRepository;

	@Mock
	private SupplierRegistrationRepository supplierRegistrationRepository;

	@Mock
	private MockHttpServletRequest httpServletRequest;

	@Mock
	private AuthServiceClient authServiceClient;

	@Mock
	private StaffManagementRepository staffManagementRepository;

	@Mock
	private NotificationReportLogService notificationReportLogService;

	@Mock
	private CustomerRegistrationRepository customerRegistrationRepository;

	@Mock
	private AuthenticationServiceImpl authenticationServiceImpl;

	@Mock
	private JavaMailSender mailSender;

	@Mock
	MimeMessage mimeMessage;

	@Mock
	private LoadingCache<String, Integer> otpCache;

	private UserAddressDetails userAddressDetails;

	@BeforeEach
	public void setUp() {

		userAddressDetails = new UserAddressDetails();
		userAddressDetails.setAddressId(1l);
		userAddressDetails.setAddress("Address");
		userAddressDetails.setName("Name");
		userAddressDetails.setMobileNumber("+919999999999");
		userAddressDetails.setAlternativeMobileNumber("+919999999998");
		userAddressDetails.setCityDistrictTown("CDT");
		userAddressDetails.setCreatedAt(LocalDateTime.now());
		userAddressDetails.setDeleted(false);
		userAddressDetails.setLandmark("landmark");
		userAddressDetails.setLatitudeValue("lav");
		userAddressDetails.setLongitudeValue("lov");
		userAddressDetails.setLocation("Location");
		userAddressDetails.setPinCode("110053");
		userAddressDetails.setPrimary(true);
		userAddressDetails.setState("Bangalore");
		userAddressDetails.setSupplierId("SUP0001");
		userAddressDetails.setUpdatedAt(LocalDateTime.now());

	}// End of setUp method

	@Test
	public void addAddressDetails() {

		UserAddressDetailsPojo addressDetailsPojo = new UserAddressDetailsPojo();
		addressDetailsPojo.setSupplierId("SUP0001");

		when(userAddressDetailsRepository.findBySupplierIdAndIsDeleted(addressDetailsPojo.getSupplierId(), false))
				.thenReturn(new ArrayList<>());

		when(userAddressDetailsRepository.save(Mockito.any())).thenReturn(userAddressDetails);

		UserAddressDetails addAddressDetails = service.addAddressDetails(addressDetailsPojo);
		assertThat(addAddressDetails.getSupplierId()).isEqualTo("SUP0001");

	}// End of addAddressDetails method

	@Test
	public void getAllAddresses() {

		List<UserAddressDetails> list = new ArrayList<>();

		list.add(userAddressDetails);

		when(userAddressDetailsRepository.findBySupplierIdAndIsDeleted("SUP0001", false)).thenReturn(list);

		List<UserAddressDetails> allAddresses = service.getAllAddresses("SUP0001");

		assertThat(allAddresses).isEqualTo(list);

	}// End of getAllAddresses method

	@Test
	public void updateAddressDetails() {

		UserAddressDetailsPojo addressDetailsPojo = new UserAddressDetailsPojo();
		addressDetailsPojo.setAddressId(1l);

		when(userAddressDetailsRepository.findByAddressIdAndIsDeleted(addressDetailsPojo.getAddressId(), false))
				.thenReturn(userAddressDetails);

		when(userAddressDetailsRepository.save(Mockito.any())).thenReturn(userAddressDetails);

		UserAddressDetails updateAddressDetails = service.updateAddressDetails(addressDetailsPojo);
		assertThat(updateAddressDetails.getAddressId()).isEqualTo(1l);

	}// End of updateAddressDetails method

	@Test
	public void changePrimaryAddress() {

		List<UserAddressDetails> list = new ArrayList<>();

		list.add(userAddressDetails);

		when(userAddressDetailsRepository.findByAddressIdAndSupplierIdAndIsDeleted(1l, "SUP0001", false))
				.thenReturn(userAddressDetails);

		when(userAddressDetailsRepository.findBySupplierId("SUP0001")).thenReturn(list);

		when(userAddressDetailsRepository.save(Mockito.any())).thenReturn(userAddressDetails);

		UserAddressDetails changePrimaryAddress = service.changePrimaryAddress("SUP0001", 1l);

		assertThat(changePrimaryAddress).isEqualTo(userAddressDetails);

	}// End of changePrimaryAddress method

	@Test
	public void deleteAddress() {

		List<UserAddressDetails> list = new ArrayList<>();

		when(userAddressDetailsRepository.findByAddressIdAndSupplierIdAndIsDeleted(1l, "SUP0001", false))
				.thenReturn(userAddressDetails);

		list.add(userAddressDetails);

		when(userAddressDetailsRepository.findBySupplierIdAndIsDeleted("SUP0001", false)).thenReturn(list);

		when(userAddressDetailsRepository.save(Mockito.any())).thenReturn(userAddressDetails);

		String deleteAddress = service.deleteAddress("SUP0001", 1l);

		assertThat(deleteAddress).isEqualTo("Address deleted successfully");

	}// End of deleteAddress method

	@Test
	public void getAddressById() {

		when(userAddressDetailsRepository.findByAddressIdAndSupplierIdAndIsDeleted(1l, "SUP0001", false))
				.thenReturn(userAddressDetails);

		UserAddressDetails addressById = service.getAddressById("SUP0001", 1l);

		assertThat(addressById).isEqualTo(userAddressDetails);

	}// End of getAddressById method

	@Test
	public void forgotPassword() {

		SupplierRegistration registration = new SupplierRegistration();
		registration.setSupplierId("SUP0001");
		registration.setAccountVerified(true);
		registration.setGstin("GSTIN24235");
		registration.setCity("Bangalore");
		registration.setEmailId("abc123@gmail.com");

		StaffManagementInfo info = new StaffManagementInfo();
		info.setStaffId("STF0001");

		ForgotPasswordPojo forgotPasswordPojo = new ForgotPasswordPojo();
		forgotPasswordPojo.setUserName("abc123@gmail.com");
		forgotPasswordPojo.setUserType(EUserRole.SUPPLIER);
		forgotPasswordPojo.setNewPassword("B@1234HR");
		forgotPasswordPojo.setReEnterPassword("B@1234HR");

		UserRole role = new UserRole();
		role.setRole(EUserRole.RESELLER);
		role.setUserRoleId(0);

		UserLoginPojo loginPojo = new UserLoginPojo();
		loginPojo.setEmailId("abc123@gmail.com");
		loginPojo.setPassword("B@67890HR");
		loginPojo.setUserLoginId(0);
		loginPojo.setUserRoles(List.of(role));

		UserLoginResponse loginResponse = new UserLoginResponse();
		loginResponse.setMessage("Change Password Successfully.");
		loginResponse.setError(false);
		loginResponse.setData(loginPojo);

		lenient().when(supplierRegistrationRepository.findByEmailIdAndStatus(forgotPasswordPojo.getUserName(),
				ESupplierStatus.APPROVED)).thenReturn(registration);

		lenient().when(staffManagementRepository.findByMobileNumberAndIsDeleteAndIsMobileNumberVerified("+919999999999",
				false, true)).thenReturn(info);

		ResponseEntity<UserLoginResponse> entity = new ResponseEntity<UserLoginResponse>(loginResponse,
				HttpStatus.ACCEPTED);

		when(authServiceClient.resetPassword(forgotPasswordPojo)).thenReturn(entity);

		UserLoginPojo forgotPassword = service.forgotPassword(forgotPasswordPojo);

		assertThat(forgotPassword.getEmailId()).isEqualTo(registration.getEmailId());

	}// End of forgotPassword method

	@Test
	public void sendOtpRegistration() throws SendFailedException {

		SupplierRegistration registration = new SupplierRegistration();
		registration.setSupplierId("SUP0001");
		registration.setAccountVerified(true);
		registration.setGstin("GSTIN24235");
		registration.setCity("Bangalore");
		registration.setEmailId("abc123@gmail.com");

		lenient().when(supplierRegistrationRepository.findByMobileNumber("+919999999999")).thenReturn(registration);

		String sendOtpRegistration = service.sendOtpRegistration("+919999999999", EUserRole.RESELLER);

		assertThat(sendOtpRegistration).isEqualTo("Provide a user type as supplier or customer");

	}// End of sendOtpRegistration method

	@Test
	public void sendOtpForForgotPassword() throws Exception {

		SupplierRegistration registration = new SupplierRegistration();
		registration.setSupplierId("SUP0001");
		registration.setAccountVerified(true);
		registration.setGstin("GSTIN24235");
		registration.setCity("Bangalore");
		registration.setEmailId("abc123@gmail.com");

		StaffManagementInfo info = new StaffManagementInfo();
		info.setStaffId("STF0001");
		info.setDelete(false);
		info.setMobileNumberVerified(true);

		when(supplierRegistrationRepository.findByEmailIdAndStatus("abc123@gmail.com", ESupplierStatus.APPROVED))
				.thenReturn(registration);

		lenient().when(staffManagementRepository.findByMobileNumberAndIsDeleteAndIsMobileNumberVerified("+919999999999",
				false, true)).thenReturn(info);

		when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

		String sendOtpForForgotPassword = service.sendOtpForForgotPassword("abc123@gmail.com", EUserRole.SUPPLIER);

		assertThat(sendOtpForForgotPassword).isEqualTo(OTP_SUCCESS_MESSAGE + "email id");

	}// End of sendOtpForForgotPassword method

	@Test
	public void verifyOtp() {
		when(otpCache.getIfPresent(Mockito.any())).thenReturn(2468);
		String verifyOtp = service.verifyOtp("123", 2468);
		assertThat(verifyOtp).isEqualTo("OTP verified successfully. ");

	}// End of verifyOtp method

	@Test
	public void verifyLoginWithOtp() {

		when(otpCache.getIfPresent(Mockito.any())).thenReturn(2468);

		AuthenticationPojo authenticationPojo = new AuthenticationPojo();
		authenticationPojo.setUserName("123");
		authenticationPojo.setUserType(EUserRole.SUPPLIER);

		AuthenticationOtpPojo authenticationOtpPojo = new AuthenticationOtpPojo();
		authenticationOtpPojo.setUserName("123");
		authenticationOtpPojo.setUserType(EUserRole.SUPPLIER);

		JwtInfoPojo infoPojo = new JwtInfoPojo();
		infoPojo.setUserId("ABC");
		infoPojo.setEmailId("abc123@gmail.com");

		when(authenticationServiceImpl.verifyUser(authenticationPojo)).thenReturn(infoPojo);

		AuthenticateOtpResponse authenticateOtpResponse = new AuthenticateOtpResponse();
		authenticateOtpResponse.setError(false);
		authenticateOtpResponse.setStaff(true);
		authenticateOtpResponse.setMessage("AUTH OTP Response");

		ResponseEntity<AuthenticateOtpResponse> entity = new ResponseEntity<>(authenticateOtpResponse,
				HttpStatus.ACCEPTED);

		authenticationOtpPojo.setUserId("ABC");
		authenticationOtpPojo.setUserName("abc123@gmail.com");

		when(authServiceClient.createOtpAuthenticationToken(authenticationOtpPojo)).thenReturn(entity);

		AuthenticateOtpResponse body = entity.getBody();

		AuthenticateResponse authenticateResponse = new AuthenticateResponse();
		authenticateResponse.setError(false);
		authenticateResponse.setMessage(body.getMessage());
		authenticateResponse.setToken(body.getToken());

		AuthenticateResponse verifyloginWithOtp = service.verifyloginWithOtp("123", 2468, EUserRole.SUPPLIER);

		assertThat(verifyloginWithOtp.getMessage()).isEqualTo("AUTH OTP Response");
	}// End of verifyloginWithOtp method

	@Test
	public void addSupplier() throws UnsupportedEncodingException, MessagingException {

		SupplierRegistration registration = new SupplierRegistration();
		registration.setSupplierId("SUP0001");
		registration.setAccountVerified(true);
		registration.setGstin("GSTIN24235");
		registration.setCity("Bangalore");
		registration.setEmailId("abc123@gmail.com");

		SupplierRegistrationPojo pojo = new SupplierRegistrationPojo();

		pojo.setSupplierId("SUP0001");
		pojo.setEmailId("abc123@gmail.com");
		pojo.setFirstName("MRMRS");
		pojo.setLastName("CART");
		pojo.setBusinessName("MMCART");
		pojo.setSupplierReferralCode("SUP000RFR");
		pojo.setWished(true);

		when(supplierRegistrationRepository.findBySupplierReferralCodeAndStatus("SUP000RFR", ESupplierStatus.APPROVED))
				.thenReturn(registration);

		lenient().when(supplierRegistrationRepository.save(Mockito.any())).thenReturn(registration);

		when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

		SupplierRegistration addSupplier = service.addSupplier(pojo);

		assertThat(addSupplier.getSupplierId()).isEqualTo(registration.getSupplierId());

	}// End of addSupplier method

	@Test
	public void changePassword() {

		SupplierRegistration registration = new SupplierRegistration();
		registration.setSupplierId("SUP0001");
		registration.setAccountVerified(true);
		registration.setGstin("GSTIN24235");
		registration.setCity("Bangalore");
		registration.setEmailId("abc123@gmail.com");

		when(supplierRegistrationRepository.findByEmailIdAndStatus("abc123@gmail.com", ESupplierStatus.APPROVED))
				.thenReturn(registration);

		ChangePasswordPojo changePasswordPojo = new ChangePasswordPojo();
		changePasswordPojo.setEmailId("abc123@gmail.com");
		changePasswordPojo.setOldPassword("B@12345HR");
		changePasswordPojo.setNewPassword("B@67890HR");
		changePasswordPojo.setReEnterPassword("B@67890HR");
		changePasswordPojo.setUserType(EUserRole.SUPPLIER);

		StaffManagementInfo info = new StaffManagementInfo();
		info.setStaffId("STF0001");

		CustomerRegistration customerRegistration = new CustomerRegistration();
		customerRegistration.setCustomerId("CST0001");

		lenient().when(staffManagementRepository.findByEmailIdAndIsDelete(changePasswordPojo.getEmailId(), false))
				.thenReturn(info);

		lenient()
				.when(customerRegistrationRepository.findByEmailIdAndIsDisabled(changePasswordPojo.getEmailId(), false))
				.thenReturn(customerRegistration);

		UserRole role = new UserRole();
		role.setRole(EUserRole.RESELLER);
		role.setUserRoleId(0);

		UserLoginPojo loginPojo = new UserLoginPojo();
		loginPojo.setEmailId("abc123@gmail.com");
		loginPojo.setPassword("B@67890HR");
		loginPojo.setUserLoginId(0);
		loginPojo.setUserRoles(List.of(role));

		UserLoginResponse loginResponse = new UserLoginResponse();
		loginResponse.setMessage("Change Password Successfully.");
		loginResponse.setError(false);
		loginResponse.setData(loginPojo);

		ResponseEntity<UserLoginResponse> entity = new ResponseEntity<UserLoginResponse>(loginResponse,
				HttpStatus.ACCEPTED);

		when(authServiceClient.changePassword(changePasswordPojo)).thenReturn(entity);

		String changePassword = service.changePassword(changePasswordPojo);

		assertThat(changePassword).isEqualTo("Change Password Successfully.");

	}// End of changePassword method

	@Test
	public void updateProfile() {

		SupplierRegistration registration = new SupplierRegistration();
		registration.setSupplierId("SUP0001");
		registration.setAccountVerified(true);
		registration.setGstin("GSTIN24235");
		registration.setCity("Banagalore");

		Optional<SupplierRegistration> optional = Optional.of(registration);

		when(supplierRegistrationRepository.findById("SUP0001")).thenReturn(optional);

		List<SupplierChangesHistoryPojo> historyPojos = new ArrayList<>();

		SupplierRegistrationPojo pojo = new SupplierRegistrationPojo();
		pojo.setSupplierId("SUP0001");
		pojo.setGstin("GST12457568");
		pojo.setCity("Bangalore");

		SupplierChangesHistoryPojo supplierChangesHistoryPojo = new SupplierChangesHistoryPojo();
		supplierChangesHistoryPojo.setChangedField("gstin");
		supplierChangesHistoryPojo.setOldValue(optional.get().getGstin());
		supplierChangesHistoryPojo.setChangedValue("GST12457568");
		supplierChangesHistoryPojo.setSupplierId(optional.get().getSupplierId());
		optional.get().setGstin(optional.get().getGstin());
		optional.get().setAccountVerified(false);
		historyPojos.add(supplierChangesHistoryPojo);

		ResponseEntity<SuccessResponse> entity = new ResponseEntity<SuccessResponse>(HttpStatus.ACCEPTED);

		lenient().when(httpServletRequest.getHeader("userId")).thenReturn("ABC");

		when(notificationReportLogService.addSupplierChangesHistory(Mockito.anyString(), Mockito.any()))
				.thenReturn(entity);

		lenient().when(supplierRegistrationRepository.save(Mockito.any())).thenReturn(registration);

		SupplierRegistration updateProfile = service.updateProfile(pojo);

		assertThat(updateProfile.getSupplierId()).isEqualTo(registration.getSupplierId());

	}// End of updateProfile method

	@Test
	public void getSupplierById() {

		SupplierRegistration registration = new SupplierRegistration();
		registration.setSupplierId("SUP0001");
		registration.setAccountVerified(true);
		registration.setGstin("GSTIN24235");
		registration.setCity("Banagalore");

		when(supplierRegistrationRepository.findBySupplierIdAndStatus("SUP0001", ESupplierStatus.APPROVED))
				.thenReturn(registration);

		SupplierRegistration supplierById = service.getSupplierById("SUP0001", ESupplierStatus.APPROVED);

		assertThat(supplierById.getSupplierId()).isEqualTo(registration.getSupplierId());

	}// End of getSupplierById method

	@Test
	public void updateSupplierProfile() {

		SupplierRegistration registration = new SupplierRegistration();
		registration.setSupplierId("SUP0001");
		registration.setAccountVerified(true);
		registration.setGstin("GSTIN24235");
		registration.setCity("Banagalore");

		SupplierChangesHistoryPojo changesHistoryPojo = new SupplierChangesHistoryPojo();
		changesHistoryPojo.setSupplierId("SUP0001");
		changesHistoryPojo.setChangedField("gstin");
		changesHistoryPojo.setOldValue("GST37628361");

		lenient().when(supplierRegistrationRepository.findBySupplierIdAndStatus("SUP0001", ESupplierStatus.APPROVED))
				.thenReturn(registration);

		when(supplierRegistrationRepository.save(Mockito.any())).thenReturn(registration);

		SupplierRegistration updateSupplierProfile = service.updateSupplierProfile(changesHistoryPojo);

		assertThat(updateSupplierProfile.getSupplierId()).isEqualTo(registration.getSupplierId());

	}// End of updateSupplierProfile method

	@Test
	public void approveSupplierProfile() {

		SupplierRegistration registration = new SupplierRegistration();
		registration.setSupplierId("SUP0001");
		registration.setAccountVerified(true);

		SupplierChangesHistoryPojo changesHistoryPojo = new SupplierChangesHistoryPojo();
		changesHistoryPojo.setAccountVerified(true);

		lenient().when(supplierRegistrationRepository.findBySupplierIdAndStatus("SUP0001", ESupplierStatus.APPROVED))
				.thenReturn(registration);

		lenient().when(supplierRegistrationRepository.save(Mockito.any())).thenReturn(registration);

		boolean approveSupplierProfile = service.approveSupplierProfile(changesHistoryPojo);

		assertThat(approveSupplierProfile).isEqualTo(true);

	}// End of approveSupplierProfile method

	@Test
	public void getReferredSupplier() {

		SupplierStoreInfo info = new SupplierStoreInfo();
		info.setSupplierStoreName("ABC");

		SupplierRegistration registration = new SupplierRegistration();
		registration.setSupplierId("SUP0001");
		registration.setSupplierStoreInfo(info);

		List<SupplierRegistration> list = new ArrayList<>();
		list.add(registration);

		ReferredSupplierPojo pojo = new ReferredSupplierPojo();
		pojo.setSupplierId("SUP0001");
		pojo.setSupplierReferralCode("SRCode");
		pojo.setTotalCommissionSaved(null);
		pojo.setTotalFreeOrderCount(100l);
		pojo.setList(new ArrayList<>());

		when(supplierRegistrationRepository.findBySupplierIdAndStatus("SUP0001", ESupplierStatus.APPROVED))
				.thenReturn(registration);

		when(supplierRegistrationRepository.findByReferredById("SUP0001")).thenReturn(list);

		ReferredSupplierPojo referredSupplier = service.getReferredSupplier("SUP0001");

		assertThat(referredSupplier.getSupplierId()).isEqualTo(pojo.getSupplierId());

	}// End of getReferredSupplier method

}// End of SupplierServiceImplTest class
