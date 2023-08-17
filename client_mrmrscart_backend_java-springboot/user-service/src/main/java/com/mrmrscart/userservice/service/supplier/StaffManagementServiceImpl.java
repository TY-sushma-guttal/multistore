package com.mrmrscart.userservice.service.supplier;

import static com.mrmrscart.userservice.common.supplier.StaffManagementConstant.*;
import static com.mrmrscart.userservice.common.supplier.StaffManagementConstant.INVALID_STAFF_ID;
import static com.mrmrscart.userservice.common.supplier.StaffManagementConstant.SOMETHING_WENT_WRONG;
import static com.mrmrscart.userservice.common.supplier.SupplierConstant.*;
import static com.mrmrscart.userservice.common.supplier.SupplierConstant.SEND_MAIL;
import static com.mrmrscart.userservice.common.supplier.SupplierConstant.SUPPLIER_EMAIL_CONTACT;
import static com.mrmrscart.userservice.common.supplier.SupplierConstant.SUPPLIER_MAIL_FAILED;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import javax.mail.SendFailedException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mrmrscart.userservice.entity.supplier.EStaffStatus;
import com.mrmrscart.userservice.entity.supplier.ESupplierStatus;
import com.mrmrscart.userservice.entity.supplier.EUserRole;
import com.mrmrscart.userservice.entity.supplier.StaffManagementInfo;
import com.mrmrscart.userservice.entity.supplier.SupplierRegistration;
import com.mrmrscart.userservice.exception.supplier.RoleException;
import com.mrmrscart.userservice.exception.supplier.StaffListEmptyException;
import com.mrmrscart.userservice.exception.supplier.StaffManagementException;
import com.mrmrscart.userservice.exception.supplier.StaffManagementInfoNotFoundException;
import com.mrmrscart.userservice.exception.supplier.SupplierMobileNumberRegisteredException;
import com.mrmrscart.userservice.exception.supplier.SupplierNotFoundException;
import com.mrmrscart.userservice.feign.client.AuthServiceClient;
import com.mrmrscart.userservice.pojo.supplier.StaffManagementPojo;
import com.mrmrscart.userservice.pojo.supplier.UserLoginPojo;
import com.mrmrscart.userservice.pojo.supplier.UserRole;
import com.mrmrscart.userservice.repository.supplier.StaffManagementRepository;
import com.mrmrscart.userservice.repository.supplier.SupplierRegistrationRepository;
import com.mrmrscart.userservice.response.supplier.UserLoginResponse;

import feign.FeignException;

import static org.springframework.data.jpa.domain.Specification.*;
import static com.mrmrscart.userservice.util.StaffManagementInfoSpecification.*;

@Service
@Transactional
public class StaffManagementServiceImpl implements StaffManagementService {

	@Autowired
	private StaffManagementRepository staffManagementRepository;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private AuthServiceClient authServiceClient;

	@Autowired(required = true)
	private PasswordEncoder bcryptEncoder;

	@Autowired
	private SupplierRegistrationRepository supplierRegistrationRepository;

	/**
	 * This method is used to generate the password
	 * 
	 * @throws NoSuchAlgorithmException
	 */
	@Transactional
	private static CharSequence generatePassword(int length) throws NoSuchAlgorithmException {

		String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
		String specialCharacters = "@#$";
		String numbers = "1234567890";
		String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
		Random random = SecureRandom.getInstanceStrong();
		char[] password = new char[length];

		password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
		password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
		password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
		password[3] = numbers.charAt(random.nextInt(numbers.length()));

		for (int i = 4; i < length; i++) {
			password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
		}
		return java.nio.CharBuffer.wrap(password);
	}// end of generatePassword()

	@Transactional
	private String sendEmailId(String recipientEmail, String password) throws SendFailedException {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
			helper.setFrom(SUPPLIER_EMAIL_CONTACT, MRMRSCART_SUPPORT);
			helper.setTo(recipientEmail);
			String subject = "MrMrsCart Support";

			StaffManagementInfo getStaff = staffManagementRepository.findByEmailIdAndIsDelete(recipientEmail, false);
			String content = null;
			String loginUrl = "<a href = \"http://10.10.30.20:3010/auth/" + "userDetails" + "\">Click here to login"
					+ "</a>";
			if (getStaff != null) {
				content = "<p>Hello,</p>" + "<p>Congratulation!!!!  Your staff registration done</p>"
						+ "Here is your credentials!!!!!!!" + "<p>User name : " + recipientEmail + "</p>"
						+ "Password : " + password + "<p>Login url : " + loginUrl;
			} else {
				throw new StaffManagementInfoNotFoundException("Staff is not present. ");
			}

			helper.setSubject(subject);
			helper.setText(content, true);
			mailSender.send(message);
		} catch (StaffManagementInfoNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new SendFailedException(SUPPLIER_MAIL_FAILED);
		}
		return SEND_MAIL;
	}

	@Override
	@Transactional
	public String addStaffInfo(StaffManagementPojo data) {
		try {
			ResponseEntity<UserLoginResponse> userResponse = authServiceClient.getUserByEmailId(data.getEmailId());
			UserLoginResponse response = userResponse.getBody();
			Optional<SupplierRegistration> getSupplier = supplierRegistrationRepository.findById(data.getSupplierId());
			if (staffManagementRepository.findByMobileNumber(data.getMobileNumber()) != null
					|| supplierRegistrationRepository.findByMobileNumber(data.getMobileNumber()) != null) {
				throw new SupplierMobileNumberRegisteredException("Mobile Number Already Registered");
			}
			if (!getSupplier.isPresent()) {
				throw new SupplierNotFoundException(INVALID_SUPPLIER);
			}
			if (response.getData() != null) {
				throw new RoleException("Role is already present. ");
			} else {

				StaffManagementInfo staffInfo = new StaffManagementInfo();
				BeanUtils.copyProperties(data, staffInfo);
				SupplierRegistration supplierRegistration = getSupplier.get();
				List<StaffManagementInfo> staffManagementInfos = supplierRegistration.getStaffManagementInfos();
				staffManagementInfos.add(staffInfo);
				supplierRegistration.setStaffManagementInfos(staffManagementInfos);
				supplierRegistrationRepository.save(supplierRegistration);
				/*
				 * Code for set the role of the Staff start
				 */
				UserRole userRole = new UserRole();
				userRole.setRole(EUserRole.STAFF);

				UserLoginPojo userLoginObj = new UserLoginPojo();
				userLoginObj.setEmailId(staffInfo.getEmailId());

				// Set a random password code starts
				CharSequence password = generatePassword(8);
				userLoginObj.setPassword(bcryptEncoder.encode(password));
				// set password code ends
				List<UserRole> userRoleList = new ArrayList<>();
				userRoleList.add(userRole);
				userLoginObj.setUserRoles(userRoleList);
				authServiceClient.addUser(userLoginObj);
				/*
				 * Code for set the role of the Staff ends
				 */
				sendEmailId(staffInfo.getEmailId(), password.toString());

				return "Staff Added";
			}
		} catch (RoleException | SupplierNotFoundException | FeignException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new StaffManagementException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	@Transactional
	public StaffManagementPojo getStaffInfo(String id) {
		Optional<StaffManagementInfo> getStaff = staffManagementRepository.findById(id);
		if (getStaff.isPresent()) {
			StaffManagementPojo pojo = new StaffManagementPojo();
			BeanUtils.copyProperties(getStaff.get(), pojo);
			return pojo;
		} else {
			throw new StaffManagementInfoNotFoundException(INVALID_STAFF_ID);
		}
	}

	@Override
	@Transactional
	public StaffManagementPojo deleteStaffInfo(String id) {
		Optional<StaffManagementInfo> getStaff = staffManagementRepository.findById(id);
		if (getStaff.isPresent()) {
			getStaff.get().setDelete(true);
			StaffManagementInfo savedData = staffManagementRepository.save(getStaff.get());
			StaffManagementPojo pojo = new StaffManagementPojo();
			BeanUtils.copyProperties(savedData, pojo);
			return pojo;
		} else {
			throw new StaffManagementInfoNotFoundException(INVALID_STAFF_ID);
		}
	}

	@Override
	@Transactional
	public List<StaffManagementPojo> getAllStaffInfo(int pageNumber, int pageSize) {
		try {
			List<StaffManagementInfo> staffList = staffManagementRepository.findAll();
			if (!staffList.isEmpty()) {
				List<StaffManagementPojo> pojoList = new ArrayList<>();
				staffList.forEach(e -> {
					StaffManagementPojo pojo = new StaffManagementPojo();
					BeanUtils.copyProperties(e, pojo);
					pojoList.add(pojo);
				});
				if (pageNumber == 0) {

					return pojoList.stream().limit(pageSize).collect(Collectors.toList());

				} else {
					int skipCount = (pageNumber) * pageSize;

					return pojoList.stream().skip(skipCount).limit(pageSize).collect(Collectors.toList());
				}
			} else {
				throw new StaffListEmptyException(EMPTY_STAFF_LIST);
			}
		} catch (StaffListEmptyException e) {
			throw e;
		} catch (Exception e) {
			throw new StaffManagementException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	@Transactional
	public List<StaffManagementPojo> getAllStaffInfoBySupllierId(String supplierId, int pageNumber, int pageSize) {

		try {
			SupplierRegistration findBySupplierIdAndStatus = supplierRegistrationRepository
					.findBySupplierIdAndStatus(supplierId, ESupplierStatus.APPROVED);
			if (findBySupplierIdAndStatus == null) {
				throw new SupplierNotFoundException(INVALID_SUPPLIER);
			}
			List<StaffManagementInfo> staffList = findBySupplierIdAndStatus.getStaffManagementInfos();
			if (!staffList.isEmpty()) {
				List<StaffManagementPojo> pojoList = new ArrayList<>();
				staffList.forEach(e -> {
					StaffManagementPojo pojo = new StaffManagementPojo();
					BeanUtils.copyProperties(e, pojo);
					pojoList.add(pojo);
				});
				if (pageNumber == 0) {

					return pojoList.stream().limit(pageSize).collect(Collectors.toList());

				} else {
					int skipCount = (pageNumber) * pageSize;

					return pojoList.stream().skip(skipCount).limit(pageSize).collect(Collectors.toList());
				}
			} else {
				throw new StaffListEmptyException(EMPTY_STAFF_LIST);
			}
		} catch (StaffListEmptyException | SupplierNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new StaffManagementException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<StaffManagementInfo> staffFilter(String supplierId, int pageNumber, int pageSize, EStaffStatus type,
			String keyword) {
		SupplierRegistration findBySupplierIdAndStatus = supplierRegistrationRepository
				.findBySupplierIdAndStatus(supplierId, ESupplierStatus.APPROVED);
		if (findBySupplierIdAndStatus == null) {
			throw new SupplierNotFoundException(INVALID_SUPPLIER);
		}
		return staffManagementRepository.findAll(getStaffManagementInfoSpecification(type, supplierId, keyword),
				PageRequest.of(pageNumber, pageSize)).getContent();
	}

	private Specification<StaffManagementInfo> getStaffManagementInfoSpecification(EStaffStatus type, String supplierId,
			String keyword) {
		if (type == EStaffStatus.NAME) {
			return where(findBySupplierId(supplierId).and(findByIsDelete(false))
					.and(containsFirstName(keyword).or(containsLastName(keyword))));
		} else if (type == EStaffStatus.MOBILE_NUMBER) {
			return where(findBySupplierId(supplierId).and(findByIsDelete(false)).and(containsMobileNumber(keyword)));
		} else if (type == EStaffStatus.EMAILID) {
			return where(findBySupplierId(supplierId).and(findByIsDelete(false)).and(containsEmailId(keyword)));
		} else if (type == EStaffStatus.ALL) {
			return where(findBySupplierId(supplierId));
		} else {
			throw new StaffManagementException(INVALID_TYPE);
		}
	}

	@Override
	public StaffManagementInfo updateStaffInfo(StaffManagementPojo data) {
		try {
			StaffManagementInfo staffInfo = staffManagementRepository.findByStaffIdAndIsDelete(data.getStaffId(),
					false);
			if (staffInfo != null) {
				if (!staffInfo.getMobileNumber().equals(data.getMobileNumber())
						&& (staffManagementRepository.findByMobileNumber(data.getMobileNumber()) != null
								|| supplierRegistrationRepository.findByMobileNumber(data.getMobileNumber()) != null)) {
					throw new SupplierMobileNumberRegisteredException("Mobile Number Already Registered");
				}
				staffInfo.setFirstName(data.getFirstName());
				staffInfo.setLastName(data.getLastName());
				staffInfo.setMobileNumber(data.getMobileNumber());
				staffInfo.setStaffCapabilityList(data.getStaffCapabilityList());
				return staffManagementRepository.save(staffInfo);
			} else {
				throw new StaffManagementInfoNotFoundException(INVALID_STAFF_ID);
			}
		} catch (StaffManagementInfoNotFoundException | SupplierMobileNumberRegisteredException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new StaffManagementException(SOMETHING_WENT_WRONG);
		}
	}
}
