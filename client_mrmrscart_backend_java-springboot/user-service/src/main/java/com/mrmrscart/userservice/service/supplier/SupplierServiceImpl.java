package com.mrmrscart.userservice.service.supplier;

import static com.mrmrscart.userservice.common.customer.CustomerConstant.CUSTOMER_ALREADY_REGISTERED;
import static com.mrmrscart.userservice.common.supplier.SupplierConstant.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.HttpsURLConnection;
import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mrmrscart.userservice.audit.BaseConfigController;
import com.mrmrscart.userservice.entity.admin.AdminRegistration;
import com.mrmrscart.userservice.entity.admin.EAdminStatus;
import com.mrmrscart.userservice.entity.customer.CustomerRegistration;
import com.mrmrscart.userservice.entity.supplier.ESupplierStatus;
import com.mrmrscart.userservice.entity.supplier.EUserRole;
import com.mrmrscart.userservice.entity.supplier.StaffManagementInfo;
import com.mrmrscart.userservice.entity.supplier.SupplierRegistration;
import com.mrmrscart.userservice.entity.supplier.UserAddressDetails;
import com.mrmrscart.userservice.exception.admin.AdminNotFoundException;
import com.mrmrscart.userservice.exception.customer.CustomerException;
import com.mrmrscart.userservice.exception.supplier.InvalidOtpException;
import com.mrmrscart.userservice.exception.supplier.LoginFailedException;
import com.mrmrscart.userservice.exception.supplier.NoAddressFoundException;
import com.mrmrscart.userservice.exception.supplier.PasswordNotMatchedException;
import com.mrmrscart.userservice.exception.supplier.ResetPasswordFailedException;
import com.mrmrscart.userservice.exception.supplier.StaffManagementInfoNotFoundException;
import com.mrmrscart.userservice.exception.supplier.SupplierException;
import com.mrmrscart.userservice.exception.supplier.SupplierIdNotFoundException;
import com.mrmrscart.userservice.exception.supplier.SupplierMobileNumberRegisteredException;
import com.mrmrscart.userservice.exception.supplier.SupplierNotFoundException;
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
import com.mrmrscart.userservice.pojo.supplier.ReferredSupplierSubPojo;
import com.mrmrscart.userservice.pojo.supplier.SupplierRegistrationPojo;
import com.mrmrscart.userservice.pojo.supplier.UserAddressDetailsPojo;
import com.mrmrscart.userservice.pojo.supplier.UserLoginPojo;
import com.mrmrscart.userservice.pojo.supplier.UserRole;
import com.mrmrscart.userservice.repository.admin.AdminRegistrationRepository;
import com.mrmrscart.userservice.repository.customer.CustomerRegistrationRepository;
import com.mrmrscart.userservice.repository.supplier.StaffManagementRepository;
import com.mrmrscart.userservice.repository.supplier.SupplierRegistrationRepository;
import com.mrmrscart.userservice.repository.supplier.UserAddressDetailsRepository;
import com.mrmrscart.userservice.response.supplier.UserLoginResponse;
import com.mrmrscart.userservice.wrapper.supplier.SupplierInfoWrapper;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SupplierServiceImpl extends BaseConfigController implements SupplierService {

	@Autowired
	private UserAddressDetailsRepository userAddressDetailsRepository;

	@Autowired
	private SupplierRegistrationRepository supplierRegistrationRepository;

	private static final Integer EXPIRE_MINS = 1;

	private static final String GSTIN = "gstin";
	private static final String CITY = "city";

	private LoadingCache<String, Integer> otpCache;

	@Value("${fast2sms.api.key}")
	private String fast2SmsKey;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private AuthServiceClient authServiceClient;

	@Autowired
	private AuthenticationServiceImpl authenticationServiceImpl;

	@Autowired
	private StaffManagementRepository staffManagementRepository;

	@Autowired
	private NotificationReportLogService notificationReportLogService;

	@Autowired
	private CustomerRegistrationRepository customerRegistrationRepository;

	@Autowired
	private AdminRegistrationRepository adminRegistrationRepository;

	public SupplierServiceImpl() {
		super();
		otpCache = CacheBuilder.newBuilder().expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES)
				.build(new CacheLoader<String, Integer>() {
					public Integer load(String key) {
						return 0;
					}
				});
	}

	@Transactional
	@Override
	public UserAddressDetails addAddressDetails(UserAddressDetailsPojo addressDetailsPojo) {
		UserAddressDetails userAddressDetails = new UserAddressDetails();
		BeanUtils.copyProperties(addressDetailsPojo, userAddressDetails);
		if (userAddressDetailsRepository.findBySupplierIdAndIsDeleted(addressDetailsPojo.getSupplierId(), false)
				.isEmpty()) {
			userAddressDetails.setPrimary(true);
		}
		return userAddressDetailsRepository.save(userAddressDetails);
	}

	@Override
	public UserAddressDetails updateAddressDetails(UserAddressDetailsPojo userAddressDetailsPojo) {
		UserAddressDetails address = userAddressDetailsRepository
				.findByAddressIdAndIsDeleted(userAddressDetailsPojo.getAddressId(), false);
		if (address != null) {
			BeanUtils.copyProperties(userAddressDetailsPojo, address);
		} else {
			throw new NoAddressFoundException("Invalid Address Id");
		}
		return userAddressDetailsRepository.save(address);
	}

	@Override
	public List<UserAddressDetails> getAllAddresses(String supplierID) {

		return userAddressDetailsRepository.findBySupplierIdAndIsDeleted(supplierID, false);
	}

	@Override
	public UserAddressDetails changePrimaryAddress(String supplierId, Long addressId) {
		UserAddressDetails userAddressDetails = userAddressDetailsRepository
				.findByAddressIdAndSupplierIdAndIsDeleted(addressId, supplierId, false);
		if (userAddressDetails != null) {
			userAddressDetailsRepository.findBySupplierId(supplierId).forEach(user -> {
				if (!user.equals(userAddressDetails)) {
					user.setPrimary(false);
					userAddressDetailsRepository.save(user);
				}
			});
			userAddressDetails.setPrimary(true);
		} else {
			throw new NoAddressFoundException("No Pickup Adress found");
		}
		return userAddressDetailsRepository.save(userAddressDetails);
	}

	@Transactional
	@Override
	public String deleteAddress(String supplierId, Long addressId) {
		UserAddressDetails userAddressDetails = userAddressDetailsRepository
				.findByAddressIdAndSupplierIdAndIsDeleted(addressId, supplierId, false);
		if (userAddressDetails != null) {
			userAddressDetails.setDeleted(true);
			userAddressDetails.setPrimary(false);
			userAddressDetailsRepository.save(userAddressDetails);
		} else {
			throw new NoAddressFoundException("Invalid Address Id");
		}
		List<UserAddressDetails> details = userAddressDetailsRepository.findBySupplierIdAndIsDeleted(supplierId, false);
		if (!details.isEmpty()) {
			details.get(0).setPrimary(true);
			userAddressDetailsRepository.save(details.get(0));
		} else {
			throw new NoAddressFoundException("No Pickup Adress found, add new Address");
		}

		return "Address deleted successfully";
	}

	@Override
	public UserAddressDetails getAddressById(String supplierId, Long addressId) {
		UserAddressDetails userAddressDetails = userAddressDetailsRepository
				.findByAddressIdAndSupplierIdAndIsDeleted(addressId, supplierId, false);
		if (userAddressDetails != null)
			return userAddressDetails;
		else
			throw new NoAddressFoundException("No Pickup Adress found");
	}

	@Override
	public UserLoginPojo forgotPassword(ForgotPasswordPojo forgotPasswordPojo) {
		try {
			if (!forgotPasswordPojo.getNewPassword().equals(forgotPasswordPojo.getReEnterPassword())) {
				throw new PasswordNotMatchedException(RE_ENTER_PASSWORD_FAILED_MESSAGE);
			}
			switch (forgotPasswordPojo.getUserType()) {
			case SUPPLIER:
				return setSupplierPassword(forgotPasswordPojo);
			case CUSTOMER:
				return forgotPasswordOfCustomer(forgotPasswordPojo);
			case ADMIN:
				return setAdminPassword(forgotPasswordPojo);
			default:
				throw new SupplierException("Provide A Valid User Type");
			}
		} catch (PasswordNotMatchedException | ResetPasswordFailedException | SupplierNotFoundException
				| CustomerException e) {
			throw e;
		} catch (Exception e) {
			throw new SupplierException(SOMETHING_WENT_WRONG);
		}
	}

	private UserLoginPojo setSupplierPassword(ForgotPasswordPojo forgotPasswordPojo) {

		if (forgotPasswordPojo.getUserName().contains("@")) {

			SupplierRegistration findByEmailIdAndStatus = supplierRegistrationRepository
					.findByEmailIdAndStatus(forgotPasswordPojo.getUserName(), ESupplierStatus.APPROVED);

			StaffManagementInfo staffManagementInfo = staffManagementRepository
					.findByEmailIdAndIsDelete(forgotPasswordPojo.getUserName(), false);

			if (!Objects.isNull(findByEmailIdAndStatus)) {
				return setPassword(forgotPasswordPojo);

			} else if (!Objects.isNull(staffManagementInfo)) {
				return setPassword(forgotPasswordPojo);

			} else {
				throw new SupplierNotFoundException(INVALID_USER);
			}
		} else {
			SupplierRegistration findByMobileNumberAndStatus = supplierRegistrationRepository
					.findByMobileNumberAndStatus(forgotPasswordPojo.getUserName(), ESupplierStatus.APPROVED);

			StaffManagementInfo staffManagementInfo = staffManagementRepository
					.findByMobileNumberAndIsDeleteAndIsMobileNumberVerified(forgotPasswordPojo.getUserName(), false,
							true);

			if (!Objects.isNull(findByMobileNumberAndStatus)) {
				forgotPasswordPojo.setUserName(findByMobileNumberAndStatus.getEmailId());
				return setPassword(forgotPasswordPojo);

			} else if (!Objects.isNull(staffManagementInfo)) {
				forgotPasswordPojo.setUserName(staffManagementInfo.getEmailId());
				return setPassword(forgotPasswordPojo);

			} else {
				throw new SupplierNotFoundException(INVALID_USER);
			}
		}
	}

	private UserLoginPojo setAdminPassword(ForgotPasswordPojo forgotPasswordPojo) {
		if (forgotPasswordPojo.getUserName().contains("@")) {
			AdminRegistration adminRegistration = adminRegistrationRepository
					.findByEmailIdAndStatusAndIsDelete(forgotPasswordPojo.getUserName(), EAdminStatus.APPROVED, false);
			if (adminRegistration != null) {
				return setPassword(forgotPasswordPojo);
			} else {
				throw new AdminNotFoundException(INVALID_USER);
			}
		} else {
			AdminRegistration adminRegistration = adminRegistrationRepository.findByMobileNumberAndStatusAndIsDelete(
					forgotPasswordPojo.getUserName(), EAdminStatus.APPROVED, false);
			if (adminRegistration != null) {
				return setPassword(forgotPasswordPojo);
			} else {
				throw new AdminNotFoundException(INVALID_USER);
			}

		}
	}

	private UserLoginPojo setPassword(ForgotPasswordPojo forgotPasswordPojo) {
		ResponseEntity<UserLoginResponse> resetPassword = authServiceClient.resetPassword(forgotPasswordPojo);
		UserLoginResponse body = resetPassword.getBody();

		if (body != null) {
			UserLoginPojo data = body.getData();
			if (data != null) {
				return data;
			} else {
				throw new ResetPasswordFailedException(RESET_PASSWORD_FAILED_MESSAGE);
			}
		} else {
			throw new ResetPasswordFailedException(RESET_PASSWORD_FAILED_MESSAGE);
		}
	}

	private UserLoginPojo forgotPasswordOfCustomer(ForgotPasswordPojo forgotPasswordPojo) {
		if (forgotPasswordPojo.getUserName().contains("@")) {
			CustomerRegistration customerRegistration = customerRegistrationRepository
					.findByEmailIdAndIsDisabled(forgotPasswordPojo.getUserName(), false);
			if (customerRegistration != null) {
				return setPassword(forgotPasswordPojo);
			} else {
				throw new CustomerException(INVALID_USER);
			}
		} else {
			CustomerRegistration customerRegistration = customerRegistrationRepository
					.findByMobileNumberAndIsDisabled(forgotPasswordPojo.getUserName(), false);
			if (customerRegistration != null) {
				forgotPasswordPojo.setUserName(customerRegistration.getEmailId());
				return setPassword(forgotPasswordPojo);
			} else {
				throw new CustomerException(INVALID_USER);
			}
		}
	}

	@Transactional
	@Override
	public String sendOtpRegistration(String mobileNumber, EUserRole userType) throws SendFailedException {
		try {
			if (userType == EUserRole.SUPPLIER) {
				SupplierRegistration findByMobileNumber = findByMobileNumber(mobileNumber);
				if (Objects.isNull(findByMobileNumber)) {
					sendSms(mobileNumber);
					return OTP_SUCCESS_MESSAGE + MOBILE_NUMBER;
				} else {
					throw new SupplierMobileNumberRegisteredException(SUPPLIER_ALREADY_REGISTERED);
				}
			} else if (userType == EUserRole.CUSTOMER) {
				CustomerRegistration findByMobileNumber = customerRegistrationRepository
						.findByMobileNumber(mobileNumber);
				if (findByMobileNumber == null) {
					sendSms(mobileNumber);
					return OTP_SUCCESS_MESSAGE + MOBILE_NUMBER;
				} else {
					throw new CustomerException(CUSTOMER_ALREADY_REGISTERED);
				}
			}

			else {
				return "Provide a user type as supplier or customer";
			}

		} catch (SupplierMobileNumberRegisteredException | CustomerException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SupplierException(SOMETHING_WENT_WRONG);
		}
	}

//	public String sendEmailId(String recipientEmail) throws MessagingException, UnsupportedEncodingException {
//
//		try {
//			MimeMessage message = mailSender.createMimeMessage();
//			MimeMessageHelper helper = new MimeMessageHelper(message);
//			helper.setFrom(SUPPLIER_EMAIL_CONTACT, MRMRSCART_SUPPORT);
//			helper.setTo(recipientEmail);
//			String subject = "MrMrsCart Support";
//			SupplierRegistration findByEmail = supplierRegistrationRepository.findByEmail(recipientEmail);
//			String supplierId = findByEmail.getSupplierId();
//
//			String password = supplierId + "," + recipientEmail;
//			String encodedUrl = Base64.getUrlEncoder().encodeToString(password.getBytes());
//
//			String content = "<p>Hello,</p>" + "<p>Your verification is completed </p>"
//					+ "<p>You are left with two more steps</p>" + "Link:" + "http:/localhost:3000/" + "?" + encodedUrl;
//			helper.setSubject(subject);
//			helper.setText(content, true);
//			mailSender.send(message);
//		} catch (Exception e) {
//			throw new SendFailedException(SUPPLIER_MAIL_FAILED);
//		}
//		return SEND_MAIL;
//	} // end of sendEmailId()

	private String sendWelcomeMail(String recipientEmail) throws MessagingException, UnsupportedEncodingException {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
			helper.setFrom(SUPPLIER_EMAIL_CONTACT, MRMRSCART_SUPPORT);
			helper.setTo(recipientEmail);
			String subject = "Greetings";
			String content = WELCOME_MAIL_MESSAGE;
			helper.setSubject(subject);
			helper.setText(content, true);
			mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SendFailedException(SUPPLIER_MAIL_FAILED);
		}
		return SEND_MAIL;
	}// end of sendEmailId()

	private String otpGenerate(String userName) {
		SecureRandom rand = new SecureRandom();
		int low = 1000;
		int high = 10000;
		int otp = rand.nextInt(high - low) + low;
		otpCache.put(userName, otp);
		return MRMRSCART_OTP_MESSAGE + otp + MRMRSCART_OTP_EXPIRE_MESSAGE;
	}// end of otpGenerate()

	private SupplierRegistration findByMobileNumber(String mobileNumber) {
		return supplierRegistrationRepository.findByMobileNumber(mobileNumber);
	}

	private StringBuilder sendSms(String number) throws SendFailedException {
		try {
			String message = otpGenerate(number);

			String apiKey = fast2SmsKey;
			String sendId = "FSTSMS";
			message = URLEncoder.encode(message, "UTF-8");
			String language = "english";
			String route = "v3";

			String myUrl = "https://www.fast2sms.com/dev/bulkV2?authorization=" + apiKey + "&sender_id=" + sendId
					+ "&message=" + message + "&language=" + language + "&route=" + route + "&numbers=" + number;

			URL url = new URL(myUrl);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			con.setRequestProperty("cache-control", "no-cache");
			StringBuilder response = new StringBuilder();
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			while (true) {
				String line = br.readLine();
				if (line == null) {
					break;
				}
				response.append(line);
			}
			return response;

		} catch (Exception e) {
			throw new SendFailedException(OTP_FAIL_MESSAGE);
		}

	}// end of sendSms()

	@Transactional
	@Override
	public String sendOtpForForgotPassword(String userName, EUserRole userType)
			throws UnsupportedEncodingException, MessagingException {
		try {
			switch (userType) {
			case SUPPLIER:
				return sendOtpForForgotPasswordToSupplier(userName);

			case CUSTOMER:
				return sendOtpForForgotPasswordToCustomer(userName);

			case ADMIN:
				return sendOtpForForgotPasswordToAdmin(userName);

			default:
				throw new SupplierNotFoundException(INVALID_USER);
			}
		} catch (SupplierNotFoundException | SupplierMobileNumberRegisteredException | CustomerException
				| SendFailedException e) {
			throw e;
		} catch (Exception exception) {
			exception.printStackTrace();
			throw new SupplierException(SOMETHING_WENT_WRONG);
		}
	}

	private String sendOtpForForgotPasswordToSupplier(String userName) throws SendFailedException {
		if (userName.contains("@")) {
			SupplierRegistration supplierRegistration = supplierRegistrationRepository.findByEmailIdAndStatus(userName,
					ESupplierStatus.APPROVED);

			StaffManagementInfo staffManagementInfo = staffManagementRepository.findByEmailIdAndIsDelete(userName,
					false);

			if (!Objects.isNull(supplierRegistration) || !Objects.isNull(staffManagementInfo)) {
				sendOtpToMail(userName);
				return OTP_SUCCESS_MESSAGE + EMAIL_ID;
			} else {
				throw new SupplierNotFoundException(INVALID_USER);
			}

		} else {
			SupplierRegistration supplierRegistration = supplierRegistrationRepository
					.findByMobileNumberAndStatus(userName, ESupplierStatus.APPROVED);
			StaffManagementInfo staffManagementInfo = staffManagementRepository
					.findByMobileNumberAndIsDeleteAndIsMobileNumberVerified(userName, false, true);
			if (!Objects.isNull(supplierRegistration) || !Objects.isNull(staffManagementInfo)) {
				sendSms(userName);
				return OTP_SUCCESS_MESSAGE + MOBILE_NUMBER;
			} else {
				throw new SupplierMobileNumberRegisteredException(INVALID_USER);
			}
		}
	}

	private String sendOtpForForgotPasswordToCustomer(String userName) throws MessagingException {
		if (userName.contains("@")) {
			CustomerRegistration customerRegistration = customerRegistrationRepository
					.findByEmailIdAndIsDisabled(userName, false);
			if (customerRegistration != null) {
				sendOtpToMail(userName);
				return OTP_SUCCESS_MESSAGE + EMAIL_ID;
			} else {
				throw new CustomerException(INVALID_USER);
			}
		} else {
			CustomerRegistration customerRegistration = customerRegistrationRepository
					.findByMobileNumberAndIsDisabled(userName, false);
			if (customerRegistration != null) {
				sendSms(userName);
				return OTP_SUCCESS_MESSAGE + MOBILE_NUMBER;
			} else {
				throw new CustomerException(INVALID_USER);
			}
		}
	}

	private String sendOtpForForgotPasswordToAdmin(String userName) throws MessagingException {
		if (userName.contains("@")) {
			AdminRegistration adminRegistration = adminRegistrationRepository
					.findByEmailIdAndStatusAndIsDelete(userName, EAdminStatus.APPROVED, false);
			if (adminRegistration != null) {
				sendOtpToMail(userName);
				return OTP_SUCCESS_MESSAGE + EMAIL_ID;
			} else {
				throw new CustomerException(INVALID_USER);
			}
		} else {
			AdminRegistration customerRegistration = adminRegistrationRepository
					.findByMobileNumberAndStatusAndIsDelete(userName, EAdminStatus.APPROVED, false);
			if (customerRegistration != null) {
				sendSms(userName);
				return OTP_SUCCESS_MESSAGE + MOBILE_NUMBER;
			} else {
				throw new CustomerException(INVALID_USER);
			}
		}
	}

	private String sendOtpToMail(String recipientMail) throws SendFailedException {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
			helper.setFrom(SUPPLIER_EMAIL_CONTACT, MRMRSCART_SUPPORT);
			helper.setTo(recipientMail);
			String subject = ONE_TIME_PASSWORD;
			String content = "<p>Hello,</p>" + "<p>You requested for the otp </p>" + "<br>" + "otp: "
					+ otpGenerate(recipientMail);
			helper.setSubject(subject);
			helper.setText(content, true);
			mailSender.send(message);
		} catch (Exception e) {
			throw new SendFailedException(SUPPLIER_MAIL_FAILED);
		}
		return SEND_MAIL;
	}

	@Transactional
	@Override
	public String verifyOtp(String userName, int otp) {
		try {
			Integer ifPresent = otpCache.getIfPresent(userName);
			if (ifPresent == otp) {
				return VERIFY_OTP_SUCCESS_MESSAGE;
			} else {
				throw new InvalidOtpException(VERIFY_OTP_FAIL_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidOtpException(VERIFY_OTP_FAIL_MESSAGE);
		}
	}

	@Override
	@Transactional
	public SupplierRegistration addSupplier(SupplierRegistrationPojo supplierRegistrationPojo)
			throws UnsupportedEncodingException, MessagingException {
		log.debug(DEBUG_MESSAGE);
		String referredById = null;
		try {
			if (!supplierRegistrationPojo.getSupplierReferralCode().isEmpty()) {
				referredById = checkSupplierReferralCode(supplierRegistrationPojo.getSupplierReferralCode());
			}
			if (!supplierRegistrationPojo.isWished()) {
				return isMergedHelper(supplierRegistrationPojo, referredById);

			} else {
				List<SupplierRegistration> findByGstinAndStatusNot = supplierRegistrationRepository
						.findByGstinAndStatusNot(supplierRegistrationPojo.getGstin(), ESupplierStatus.REJECTED);
				if (findByGstinAndStatusNot.isEmpty()) {

					SupplierRegistration supplierRegistration = new SupplierRegistration();
					BeanUtils.copyProperties(supplierRegistrationPojo, supplierRegistration, "supplierReferralCode");
					supplierRegistration.setStatus(ESupplierStatus.INITIATED);
					supplierRegistration.setRegisteredAt(LocalDateTime.now());
					supplierRegistration.setReferredById(referredById);
					SupplierRegistration save = supplierRegistrationRepository.save(supplierRegistration);
					sendWelcomeMail(supplierRegistrationPojo.getEmailId());
					return save;
				} else {
					throw new SupplierException(GSTIN_EXIST);
				}
			}
		} catch (SupplierException e) {
			log.error(ERROR_MESSAGE);
			throw e;
		} catch (Exception e) {
			log.error(ERROR_MESSAGE);
			e.printStackTrace();
			throw new SupplierException(SOMETHING_WENT_WRONG);
		}
	}

	private SupplierRegistration isMergedHelper(SupplierRegistrationPojo supplierRegistrationPojo, String referredById)
			throws UnsupportedEncodingException, MessagingException {
		ResponseEntity<UserLoginResponse> userByEmailId = authServiceClient
				.getUserByEmailId(supplierRegistrationPojo.getEmailId());
		UserLoginResponse body = userByEmailId.getBody();
		if (body != null) {
			if (body.getData() != null) {
				UserLoginPojo pojo = new UserLoginPojo();
				BeanUtils.copyProperties(body.getData(), pojo);
				return addSupplierHelper(supplierRegistrationPojo, pojo, referredById);
			} else {
				List<SupplierRegistration> findByGstinAndStatusNot = supplierRegistrationRepository
						.findByGstinAndStatusNot(supplierRegistrationPojo.getGstin(), ESupplierStatus.REJECTED);
				if (findByGstinAndStatusNot.isEmpty()) {
					checkSupplierEmailId(supplierRegistrationPojo.getEmailId(),
							supplierRegistrationPojo.getMobileNumber());
					SupplierRegistration supplierRegistration = new SupplierRegistration();
					BeanUtils.copyProperties(supplierRegistrationPojo, supplierRegistration, "supplierReferralCode");
					supplierRegistration.setStatus(ESupplierStatus.INITIATED);
					supplierRegistration.setRegisteredAt(LocalDateTime.now());
					ResponseEntity<UserLoginResponse> addUserEmail = authServiceClient
							.addUserEmail(supplierRegistrationPojo.getEmailId());
					UserLoginResponse body2 = addUserEmail.getBody();
					if (body2 != null && body2.getData() == null) {
						throw new SupplierException(USER_LOGIN_FAILURE);
					}
					supplierRegistration.setReferredById(referredById);
					SupplierRegistration save = supplierRegistrationRepository.save(supplierRegistration);
					sendWelcomeMail(supplierRegistrationPojo.getEmailId());
					return save;
				} else {
					throw new SupplierException(GSTIN_EXIST);
				}
			}
		} else {
			throw new SupplierException(SOMETHING_WENT_WRONG);
		}
	}

	private SupplierRegistration addSupplierHelper(SupplierRegistrationPojo supplierRegistrationPojo,
			UserLoginPojo data, String referredById) throws UnsupportedEncodingException, MessagingException {
		List<UserRole> userRoles = data.getUserRoles();
		if (userRoles.isEmpty()) {
			List<SupplierRegistration> findByGstinAndStatusNot = supplierRegistrationRepository
					.findByGstinAndStatusNot(supplierRegistrationPojo.getGstin(), ESupplierStatus.REJECTED);
			if (findByGstinAndStatusNot.isEmpty()) {
				checkSupplierEmailId(supplierRegistrationPojo.getEmailId(), supplierRegistrationPojo.getMobileNumber());
				SupplierRegistration supplierRegistration = new SupplierRegistration();
				BeanUtils.copyProperties(supplierRegistrationPojo, supplierRegistration, "supplierReferralCode");
				supplierRegistration.setStatus(ESupplierStatus.INITIATED);
				supplierRegistration.setRegisteredAt(LocalDateTime.now());
				supplierRegistration.setReferredById(referredById);
				SupplierRegistration save = supplierRegistrationRepository.save(supplierRegistration);
				sendWelcomeMail(supplierRegistrationPojo.getEmailId());
				return save;
			} else {
				throw new SupplierException(GSTIN_EXIST);
			}
		} else {
			long count = userRoles.stream().filter(e -> e.getRole() == (EUserRole.SUPPLIER)).count();
			long count2 = userRoles.stream().filter(e -> e.getRole() == EUserRole.STAFF).count();
			if (count > 0) {
				throw new SupplierException(EMAIL_EXIST);
			} else if (count2 > 0) {
				throw new SupplierException(STAFF_ERROR);
			} else {
				StringBuilder stringBuilder = new StringBuilder();
				userRoles.forEach(e -> stringBuilder.append(e.getRole() + ", "));
				throw new SupplierException(ACCOUNT_EXIST + stringBuilder + WISH_CONTINUE);
			}
		}
	}

	private String checkSupplierReferralCode(String supplierReferralCode) {
		SupplierRegistration findBySupplierReferralCodeAndStatus = supplierRegistrationRepository
				.findBySupplierReferralCodeAndStatus(supplierReferralCode, ESupplierStatus.APPROVED);
		if (findBySupplierReferralCodeAndStatus != null) {
			return findBySupplierReferralCodeAndStatus.getSupplierId();
		} else {
			throw new SupplierException(INVALID_REFER_CODE);
		}
	}

	private void checkSupplierEmailId(String emailId, String mobileNumber) {
		List<SupplierRegistration> findByEmailId = supplierRegistrationRepository.findByEmailIdOrMobileNumber(emailId,
				mobileNumber);
		if (!findByEmailId.isEmpty()) {
			throw new SupplierException(USER_EXIST);
		}
	}

	@Transactional
	@Override
	public AuthenticateResponse verifyloginWithOtp(String userName, int otp, EUserRole userType) {
		try {
			String verifyloginWithOtp = verifyOtp(userName, otp);

			if (verifyloginWithOtp.equals(VERIFY_OTP_SUCCESS_MESSAGE)) {

				// token generation method to be called
				AuthenticationPojo authenticationPojo = new AuthenticationPojo();
				authenticationPojo.setUserName(userName);
				authenticationPojo.setUserType(userType);
				AuthenticationOtpPojo authenticationOtpPojo = new AuthenticationOtpPojo();
				authenticationOtpPojo.setUserName(userName);
				authenticationOtpPojo.setUserType(userType);
				JwtInfoPojo verifyUser = authenticationServiceImpl.verifyUser(authenticationPojo);
				authenticationOtpPojo.setUserId(verifyUser.getUserId());
				authenticationOtpPojo.setUserName(verifyUser.getEmailId());
				ResponseEntity<AuthenticateOtpResponse> createOtpAuthenticationToken = authServiceClient
						.createOtpAuthenticationToken(authenticationOtpPojo);
				AuthenticateOtpResponse body = createOtpAuthenticationToken.getBody();
				if (body != null) {
					AuthenticateResponse authenticateResponse = new AuthenticateResponse();
					authenticateResponse.setError(false);
					authenticateResponse.setMessage(body.getMessage());
					authenticateResponse.setToken(body.getToken());

					if (body.isStaff()) {

						StaffManagementInfo findByEmailIdAndIsDelete = staffManagementRepository
								.findByEmailIdAndIsDelete(verifyUser.getEmailId(), false);
						authenticateResponse.setStaffDetails(findByEmailIdAndIsDelete);
					}

					return authenticateResponse;

				} else {
					throw new LoginFailedException("Login Failed!");
				}

			} else {
				throw new InvalidOtpException(VERIFY_OTP_FAIL_MESSAGE);
			}

		} catch (SupplierNotFoundException | LoginFailedException | InvalidOtpException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidOtpException(VERIFY_OTP_FAIL_MESSAGE);
		}
	}

	@Override
	public String changePassword(ChangePasswordPojo changePasswordPojo) {
		try {
			checkReEnterPassword(changePasswordPojo.getNewPassword(), changePasswordPojo.getReEnterPassword());

			if (changePasswordPojo.getUserType() == EUserRole.SUPPLIER) {
				findByEmailAndStatus(changePasswordPojo.getEmailId());
				return changePasswordInAuth(changePasswordPojo);
			} else if (changePasswordPojo.getUserType() == EUserRole.STAFF) {
				StaffManagementInfo staff = staffManagementRepository
						.findByEmailIdAndIsDelete(changePasswordPojo.getEmailId(), false);
				if (Objects.isNull(staff)) {
					throw new StaffManagementInfoNotFoundException(INVALID_USER);
				} else {
					return changePasswordInAuth(changePasswordPojo);
				}

			} else if (changePasswordPojo.getUserType() == EUserRole.CUSTOMER) {
				CustomerRegistration customerRegistration = customerRegistrationRepository
						.findByEmailIdAndIsDisabled(changePasswordPojo.getEmailId(), false);
				if (customerRegistration != null) {
					return changePasswordInAuth(changePasswordPojo);
				} else {
					throw new CustomerException(INVALID_USER);
				}
			} else {
				throw new SupplierException("Provide user type as SUPPLIER or STAFF or CUSTOMER");

			}
		} catch (SupplierNotFoundException | PasswordNotMatchedException | CustomerException | FeignException e) {
			throw e;
		}

		catch (Exception e) {
			e.printStackTrace();
			throw new SupplierException(SOMETHING_WENT_WRONG);
		}
	}

	private SupplierRegistration findByEmailAndStatus(String emailId) {

		SupplierRegistration supplierRegistration = supplierRegistrationRepository.findByEmailIdAndStatus(emailId,
				ESupplierStatus.APPROVED);
		if (Objects.isNull(supplierRegistration)) {
			throw new SupplierNotFoundException("Supplier Not Found");
		} else {
			return supplierRegistration;
		}
	}

	private String checkReEnterPassword(String newPassword, String reEnterPassword) {
		if (newPassword.equals(reEnterPassword)) {
			return SUCCESS;
		} else {
			throw new PasswordNotMatchedException(RE_ENTER_PASSWORD_FAILED_MESSAGE);
		}
	}

	private String changePasswordInAuth(ChangePasswordPojo changePasswordPojo) {
		ResponseEntity<UserLoginResponse> changePassword = authServiceClient.changePassword(changePasswordPojo);
		UserLoginResponse body = changePassword.getBody();
		if (body != null) {
			if (!body.isError()) {
				return body.getMessage();
			} else {
				throw new PasswordNotMatchedException(RESET_PASSWORD_FAILED_MESSAGE);
			}
		} else {
			throw new PasswordNotMatchedException(RESET_PASSWORD_FAILED_MESSAGE);
		}
	}

	@Override
	public SupplierRegistration updateProfile(SupplierRegistrationPojo supplierRegistrationPojo) {

		Optional<SupplierRegistration> optional = supplierRegistrationRepository
				.findById(supplierRegistrationPojo.getSupplierId());
		if (optional.isPresent()) {
			List<SupplierChangesHistoryPojo> historyPojos = new ArrayList<>();
			if (!optional.get().getGstin().equals(supplierRegistrationPojo.getGstin())) {
				SupplierChangesHistoryPojo supplierChangesHistoryPojo = new SupplierChangesHistoryPojo();
				supplierChangesHistoryPojo.setChangedField(GSTIN);
				supplierChangesHistoryPojo.setOldValue(optional.get().getGstin());
				supplierChangesHistoryPojo.setChangedValue(supplierRegistrationPojo.getGstin());
				supplierChangesHistoryPojo.setSupplierId(optional.get().getSupplierId());
				optional.get().setGstin(optional.get().getGstin());
				optional.get().setAccountVerified(false);
				historyPojos.add(supplierChangesHistoryPojo);
			}
			if (!optional.get().getCity().equalsIgnoreCase(supplierRegistrationPojo.getCity())) {
				SupplierChangesHistoryPojo supplierChangesHistoryPojo = new SupplierChangesHistoryPojo();
				supplierChangesHistoryPojo.setChangedField(CITY);
				supplierChangesHistoryPojo.setOldValue(optional.get().getCity());
				supplierChangesHistoryPojo.setChangedValue(supplierRegistrationPojo.getCity());
				supplierChangesHistoryPojo.setSupplierId(optional.get().getSupplierId());
				optional.get().setCity(optional.get().getCity());
				optional.get().setAccountVerified(false);
				historyPojos.add(supplierChangesHistoryPojo);
			}
			BeanUtils.copyProperties(supplierRegistrationPojo, optional.get(), "supplierId");
			SupplierRegistration save = supplierRegistrationRepository.save(optional.get());
			historyPojos.forEach(pojo -> pojo.setUpdatedAt(save.getLastModifiedDate()));
			notificationReportLogService.addSupplierChangesHistory(getUserId(), historyPojos);
			return save;
		} else {
			throw new SupplierNotFoundException("No Supplier Profile found");
		}
	}

	@Override
	public SupplierRegistration getSupplierById(String id, ESupplierStatus status) {
		try {
			SupplierRegistration getSupplier = supplierRegistrationRepository.findBySupplierIdAndStatus(id, status);
			if (getSupplier != null) {
				return getSupplier;
			} else {
				throw new SupplierIdNotFoundException("Supplier id not present. ");
			}
		} catch (SupplierIdNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new SupplierException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public SupplierRegistration updateSupplierProfile(SupplierChangesHistoryPojo supplierChangesHistoryPojo) {
		SupplierRegistration supplier = supplierRegistrationRepository
				.findBySupplierIdAndStatus(supplierChangesHistoryPojo.getSupplierId(), ESupplierStatus.APPROVED);
		if (supplier != null) {
			if (supplierChangesHistoryPojo.getChangedField().equalsIgnoreCase(GSTIN)) {
				supplier.setGstin(supplierChangesHistoryPojo.getOldValue());
			}
			if (supplierChangesHistoryPojo.getChangedField().equalsIgnoreCase(CITY)) {
				supplier.setCity(supplierChangesHistoryPojo.getOldValue());
			}
			if (supplierChangesHistoryPojo.isAccountVerified()) {
				supplier.setAccountVerified(true);
			}
			return supplierRegistrationRepository.save(supplier);
		} else {
			throw new SupplierIdNotFoundException(SUPPLIER_NOT_FOUND);
		}

	}

	@Override
	public boolean approveSupplierProfile(SupplierChangesHistoryPojo supplierChangesHistoryPojo) {
		SupplierRegistration supplier = supplierRegistrationRepository
				.findBySupplierIdAndStatus(supplierChangesHistoryPojo.getSupplierId(), ESupplierStatus.APPROVED);
		if (supplier != null) {
			if (supplierChangesHistoryPojo.isAccountVerified()) {
				supplier.setAccountVerified(true);
			}
			supplierRegistrationRepository.save(supplier);
		}
		return true;
	}

	@Override
	public ReferredSupplierPojo getReferredSupplier(String supplierId) {
		try {
			SupplierRegistration supplierRegistration = supplierRegistrationRepository
					.findBySupplierIdAndStatus(supplierId, ESupplierStatus.APPROVED);
			if (supplierRegistration != null) {
				ReferredSupplierPojo referredSupplierPojo = new ReferredSupplierPojo();
				BeanUtils.copyProperties(supplierRegistration, referredSupplierPojo);
				referredSupplierPojo.setTotalCommissionSaved(null); // after orders complete set this
				List<ReferredSupplierSubPojo> list = new ArrayList<>();
				List<SupplierRegistration> findByReferredById = supplierRegistrationRepository
						.findByReferredById(supplierId);
				findByReferredById.forEach(e -> {
					ReferredSupplierSubPojo referredSupplierSubPojo = new ReferredSupplierSubPojo();
					BeanUtils.copyProperties(e, referredSupplierSubPojo);
					referredSupplierSubPojo.setSupplierStoreName(e.getSupplierStoreInfo().getSupplierStoreName());
					list.add(referredSupplierSubPojo);
				});
				referredSupplierPojo.setList(list);
				return referredSupplierPojo;
			} else {
				throw new SupplierIdNotFoundException(SUPPLIER_NOT_FOUND);
			}
		} catch (SupplierIdNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new SupplierException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public SupplierInfoWrapper findSupplierAndStoreInfo(String supplierId, ESupplierStatus status) {
		try {
			SupplierRegistration supplierRegistration = supplierRegistrationRepository
					.findBySupplierIdAndStatus(supplierId, status);
			if (supplierRegistration != null) {
				SupplierInfoWrapper supplierInfoWrapper = new SupplierInfoWrapper();
				BeanUtils.copyProperties(supplierRegistration, supplierInfoWrapper);
				supplierInfoWrapper
						.setSupplierStoreCode(supplierRegistration.getSupplierStoreInfo().getSupplierStoreCode());
				if (supplierRegistration.getUserAddressDetails().isEmpty()) {
					supplierInfoWrapper.setAddressPresent(false);
					return supplierInfoWrapper;
				} else {
					supplierInfoWrapper.setAddressPresent(true);
					return supplierInfoWrapper;
				}
			} else {
				throw new SupplierIdNotFoundException(INVALID_SUPPLIER);
			}
		} catch (SupplierIdNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new SupplierException(SOMETHING_WENT_WRONG);
		}
	}

	public SupplierRegistration getSupplierById(String id) {
		try {
			Optional<SupplierRegistration> getSupplier = supplierRegistrationRepository.findById(id);
			if (getSupplier.isPresent()) {
				return getSupplier.get();
			} else {
				throw new SupplierIdNotFoundException("Supplier id not present. ");
			}
		} catch (SupplierIdNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new SupplierException(SOMETHING_WENT_WRONG);
		}
	}

}
