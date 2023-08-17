package com.mrmrscart.userservice.service.admin;

import static com.mrmrscart.userservice.common.supplier.SupplierConstant.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import javax.mail.SendFailedException;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.HttpsURLConnection;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.mrmrscart.userservice.entity.admin.AdminConfiguration;
import com.mrmrscart.userservice.entity.admin.EAdminStatus;
import com.mrmrscart.userservice.entity.supplier.ESupplierStatus;
import com.mrmrscart.userservice.entity.supplier.EUserRole;
import com.mrmrscart.userservice.entity.supplier.SupplierRegistration;
import com.mrmrscart.userservice.entity.supplier.SupplierStoreInfo;
import com.mrmrscart.userservice.entity.supplier.SupplierStoreTheme;
import com.mrmrscart.userservice.exception.admin.AdminException;
import com.mrmrscart.userservice.exception.supplier.StaffManagementException;
import com.mrmrscart.userservice.exception.supplier.SupplierException;
import com.mrmrscart.userservice.exception.supplier.SupplierNotFoundException;
import com.mrmrscart.userservice.feign.client.AuthServiceClient;
import com.mrmrscart.userservice.pojo.admin.FromAndToDatePojo;
import com.mrmrscart.userservice.pojo.admin.SupplierApprovalPojo;
import com.mrmrscart.userservice.pojo.admin.SupplierCountPojo;
import com.mrmrscart.userservice.pojo.admin.UserRolePojo;
import com.mrmrscart.userservice.repository.admin.AdminConfigurationRepository;
import com.mrmrscart.userservice.repository.supplier.SupplierRegistrationRepository;
import com.mrmrscart.userservice.repository.supplier.SupplierStoreThemeRepository;
import com.mrmrscart.userservice.util.PaginatedResponse;
import com.mrmrscart.userservice.wrapper.admin.AdminSupplierCountWrapper;

@Service
public class AdminSupplierServiceImpl implements AdminSupplierService {

	@Autowired
	private SupplierRegistrationRepository supplierRegistrationRepository;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private AuthServiceClient authServiceClient;

	@Autowired
	private AdminConfigurationRepository adminConfigurationRepository;

	@Value("${fast2sms.api.key}")
	private String fast2SmsKey;

	@Autowired
	private SupplierStoreThemeRepository storeThemeRepository;

	private String sendEmailId(String recipientEmail) throws SendFailedException {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
			helper.setFrom(SUPPLIER_EMAIL_CONTACT, MRMRSCART_SUPPORT);
			helper.setTo(recipientEmail);
			String subject = "MrMrsCart Support";
			SupplierRegistration findByEmail = supplierRegistrationRepository.findByEmailId(recipientEmail);
			String password = LocalDateTime.now() + "," + recipientEmail + "," + findByEmail.getSupplierId();
			String encodedUrl = Base64.getUrlEncoder().encodeToString(password.getBytes());

			if (findByEmail.getStatus().equals(ESupplierStatus.APPROVED)) {
				String content = "<p>Hello,</p>" + "<p>Your verification is completed </p>"

						+ "<p>Please click the below link to set your credentials and proceed further</p>" + "Link: "
						+ "<a href = \"http://10.10.31.148:3010/auth/supplier/newpassword?user=" + encodedUrl
						+ "\">http://10.10.31.148:3010/auth/supplier/newpassword?user=" + encodedUrl + "</a>";
				helper.setSubject(subject);
				helper.setText(content, true);
				mailSender.send(message);
			} else {
				String content = "<p>Dear Supplier,</p>"
						+ "<p>MrMrsCart has completed its analysis of the proposal submited. "
						+ "We regret to inform you that after careful consideration, "
						+ "we determined that your proposal was not the most advantageous to the company at this time."
						+ "Our decision based on technical, price and timeline factors,in addition to a vendor's "
						+ "ability to understand our business.</p>";
				helper.setSubject(subject);
				helper.setText(content, true);
				mailSender.send(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SendFailedException(SUPPLIER_MAIL_FAILED);
		}
		return SEND_MAIL;
	}

	@Override
	@Transactional
	public SupplierRegistration supplierApproval(SupplierApprovalPojo data) throws SendFailedException {
		try {
			SupplierRegistration supplier = supplierRegistrationRepository
					.findBySupplierIdAndStatus(data.getSupplierId(), ESupplierStatus.INITIATED);
			if (supplier != null) {

				if (data.getStatus() == ESupplierStatus.APPROVED) {

					supplier.setStatus(ESupplierStatus.APPROVED);
					String supplierCode = getSupplierCode();
					supplier.setSupplierCode(supplierCode);
					StringBuilder uniqueCode = new StringBuilder(
							UUID.randomUUID().toString().substring(0, 4).toUpperCase());
					supplier.setSupplierReferralCode(MCT + supplierCode + uniqueCode);
					SupplierStoreInfo supplierStoreInfo = new SupplierStoreInfo();
					supplierStoreInfo.setSupplierStoreCode(SUP + supplierCode + STR + uniqueCode.reverse());
					SupplierStoreTheme storeTheme = storeThemeRepository.findByColorName("ORANGE");
					List<SupplierStoreTheme> arrayList = new ArrayList<>();
					arrayList.add(storeTheme);
					supplierStoreInfo.setStoreThemes(arrayList);
					supplier.setSupplierStoreInfo(supplierStoreInfo);
					long freeOrdersCount = getFreeOrdersCount(supplier);
					supplier.setRemainingFreeOrderCount(freeOrdersCount);
					supplier.setTotalFreeOrderCount(freeOrdersCount);
					supplier.setSignupFreeOrderCount(freeOrdersCount);
					supplier.setAccountVerified(true);
					supplier.setApprovedAt(LocalDateTime.now());
					SupplierRegistration approvedSupplier = supplierRegistrationRepository.save(supplier);
					/*
					 * Code for set the role of the approve supplier start
					 */
					UserRolePojo userRolePojo = new UserRolePojo();
					userRolePojo.setEmailId(approvedSupplier.getEmailId());
					userRolePojo.setRole(EUserRole.SUPPLIER);
					authServiceClient.addUserRole(userRolePojo);
					/*
					 * Code for set the role of the approve supplier end
					 */
					sendEmailId(supplier.getEmailId());
					return approvedSupplier;
				} else if (data.getStatus() == ESupplierStatus.REJECTED) {
					supplier.setStatus(data.getStatus());
					SupplierRegistration rejectedSupplier = supplierRegistrationRepository.save(supplier);
					// send mail for supplier rejection
					sendEmailId(supplier.getEmailId());
					return rejectedSupplier;
				} else {
					throw new SupplierNotFoundException("Provide A Valid Status");
				}
			} else {
				throw new SupplierNotFoundException("Supplier Not found. ");
			}
		} catch (SupplierNotFoundException | AdminException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new StaffManagementException(SOMETHING_WENT_WRONG);
		}
	}

	private String getSupplierCode() {
		SupplierRegistration findTopByOrderByApprovedAtDesc = supplierRegistrationRepository
				.findTopByOrderByApprovedAtDesc();
		if (findTopByOrderByApprovedAtDesc.getSupplierCode() != null) {
			String substring = findTopByOrderByApprovedAtDesc.getSupplierCode().substring(1);
			int parseInt = Integer.parseInt(substring) + 1;
			return S + String.format("%02d", parseInt);
		} else {
			return S + "01";
		}
	}

	private long getFreeOrdersCount(SupplierRegistration supplier) {
		AdminConfiguration adminConfiguration = adminConfigurationRepository
				.findByAdminConfigurationName(FREE_ORDERS_COUNT);
		if (adminConfiguration != null) {
			long freeOrdersCount = Long.parseLong(adminConfiguration.getAdminConfigurationValue());
			if (supplier.getReferredById() != null) {
				SupplierRegistration supplierRegistration = supplierRegistrationRepository
						.findBySupplierIdAndStatus(supplier.getReferredById(), ESupplierStatus.APPROVED);
				if (supplierRegistration != null) {
					supplierRegistration
							.setTotalFreeOrderCount(supplierRegistration.getTotalFreeOrderCount() + freeOrdersCount);
					supplierRegistration.setRemainingFreeOrderCount(
							supplierRegistration.getRemainingFreeOrderCount() + freeOrdersCount);
					supplierRegistrationRepository.save(supplierRegistration);
				}
			}
			return freeOrdersCount;
		} else {
			throw new AdminException(ADMIN_CONFIG_ERROR);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public AdminSupplierCountWrapper getAllSupplier(ESupplierStatus status, int pageNumber, int pageSize) {
		try {
			List<SupplierRegistration> findByStatus = supplierRegistrationRepository.findByStatus(status);
			AdminSupplierCountWrapper result = new AdminSupplierCountWrapper();
			if (findByStatus.isEmpty()) {
				return result;
			}
			result.setCount(findByStatus.size());
			PaginatedResponse.getPaginatedResponse(findByStatus, pageNumber, pageSize);
			result.setSupplierRegistrations((List<SupplierRegistration>) PaginatedResponse
					.getPaginatedResponse(findByStatus, pageNumber, pageSize));
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SupplierNotFoundException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<SupplierRegistration> getAllSupplierInfo(Specification<SupplierRegistration> spec, int pageNumber,
			int pageSize) {
		Page<SupplierRegistration> findAll = supplierRegistrationRepository.findAll(spec,
				PageRequest.of(pageNumber, pageSize));
		return findAll.getContent();

	}

	@Override
	public SupplierCountPojo getSupplierCount(FromAndToDatePojo fromAndToDatePojo) {
		try {
			long newSupplier = supplierRegistrationRepository
					.countByRegisteredAtBetween(fromAndToDatePojo.getFromDate(), fromAndToDatePojo.getToDate());
			long oldSupplier = supplierRegistrationRepository
					.countByRegisteredAtBefore(fromAndToDatePojo.getFromDate());
			return new SupplierCountPojo(newSupplier, oldSupplier);
		} catch (Exception e) {
			throw new AdminException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public void inviteSupplier(String userName) throws SendFailedException {
		try {
			if (userName.contains("@")) {
				SupplierRegistration findByEmailId = supplierRegistrationRepository.findByEmailId(userName);
				if (findByEmailId == null) {
					sendInvitationMail(userName);
				} else {
					throw new SupplierException(SUPPLIER_ALREADY_REGISTERED);
				}
			} else if (userName.matches(MOBILENO_REGEX)) {
				SupplierRegistration findByMobileNumber = supplierRegistrationRepository.findByMobileNumber(userName);
				if (findByMobileNumber == null) {
					sendInvitationSms(userName.substring(3));
				} else {
					throw new SupplierException(SUPPLIER_ALREADY_REGISTERED);
				}
			} else {
				throw new SupplierException(INVALID_USERNAME);
			}
		} catch (SupplierException e) {
			throw e;
		}

		catch (Exception e) {
			throw new SupplierException(SOMETHING_WENT_WRONG);
		}
	}

	private void sendInvitationMail(String recipientMail) throws SendFailedException {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
			helper.setFrom(SUPPLIER_EMAIL_CONTACT, MRMRSCART_SUPPORT);
			helper.setTo(recipientMail);
			String subject = MRMRSCART_INVITATION;
			String content = INVITATION_MAIL_MESSAGE;
			helper.setSubject(subject);
			helper.setText(content, true);
			mailSender.send(message);
		} catch (Exception e) {
			throw new SendFailedException(SUPPLIER_MAIL_FAILED);
		}
	}

	private StringBuilder sendInvitationSms(String number) throws SendFailedException {
		try {
			String message = INVITATION_MOBILE_MESSAGE;

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

	}

	@Override
	public List<SupplierRegistration> getSupplierInfoBasedOnFilter(EAdminStatus status, String category,
			String fromDate, String toDate, String keyWord) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean enableDisableSupplier(String supplierId, boolean isDisabled) {
		try {
			if (isDisabled) {
				SupplierRegistration supplier = findSupplier(supplierId, ESupplierStatus.APPROVED);
				supplier.setStatus(ESupplierStatus.DISABLED);
				supplierRegistrationRepository.save(supplier);
				// any notification to be sent?
			} else {
				SupplierRegistration supplier = findSupplier(supplierId, ESupplierStatus.DISABLED);
				supplier.setStatus(ESupplierStatus.APPROVED);
				supplierRegistrationRepository.save(supplier);
				// any notification to be sent?
			}
			return isDisabled;
		} catch (SupplierNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new SupplierException(SOMETHING_WENT_WRONG);
		}
	}

	private SupplierRegistration findSupplier(String supplierId, ESupplierStatus status) {
		try {
			SupplierRegistration supplierRegistration = supplierRegistrationRepository
					.findBySupplierIdAndStatus(supplierId, status);
			if (supplierRegistration != null) {
				return supplierRegistration;
			} else {
				throw new SupplierNotFoundException(INVALID_SUPPLIER);
			}
		} catch (Exception e) {
			throw new SupplierNotFoundException(INVALID_SUPPLIER);
		}
	}

}
