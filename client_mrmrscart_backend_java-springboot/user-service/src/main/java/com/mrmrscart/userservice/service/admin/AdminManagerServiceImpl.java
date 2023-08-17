package com.mrmrscart.userservice.service.admin;

import static com.mrmrscart.userservice.common.admin.AdminRegistrationConstant.INVALID_ADMIN_MANAGER;
import static com.mrmrscart.userservice.common.admin.AdminRegistrationConstant.SOMETHING_WENT_WRONG;
import static com.mrmrscart.userservice.common.supplier.SupplierConstant.MRMRSCART_SUPPORT;
import static com.mrmrscart.userservice.common.supplier.SupplierConstant.SEND_MAIL;
import static com.mrmrscart.userservice.common.supplier.SupplierConstant.SUPPLIER_EMAIL_CONTACT;
import static com.mrmrscart.userservice.common.supplier.SupplierConstant.SUPPLIER_MAIL_FAILED;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.mail.SendFailedException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.mrmrscart.userservice.entity.admin.AdminCapability;
import com.mrmrscart.userservice.entity.admin.AdminRegistration;
import com.mrmrscart.userservice.entity.admin.AdminRole;
import com.mrmrscart.userservice.entity.admin.EAdminStatus;
import com.mrmrscart.userservice.entity.admin.EDesignation;
import com.mrmrscart.userservice.exception.admin.AdminManagerNotFound;
import com.mrmrscart.userservice.exception.admin.AdminRegistrationException;
import com.mrmrscart.userservice.exception.supplier.RoleException;
import com.mrmrscart.userservice.exception.supplier.SupplierMobileNumberRegisteredException;
import com.mrmrscart.userservice.feign.client.AuthServiceClient;
import com.mrmrscart.userservice.pojo.admin.AdminManagerFilterPojo;
import com.mrmrscart.userservice.pojo.admin.AdminManagerPojo;
import com.mrmrscart.userservice.repository.admin.AdminRegistrationRepository;
import com.mrmrscart.userservice.repository.admin.AdminRoleRepository;
import com.mrmrscart.userservice.response.supplier.UserLoginResponse;
import com.mrmrscart.userservice.wrapper.admin.AdminIdAndNameWrapper;

import feign.FeignException.FeignClientException;

@Service
@Transactional
public class AdminManagerServiceImpl implements AdminManagerService {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private AdminRegistrationRepository adminRegistrationRepository;

	@Autowired
	private AdminRoleRepository adminRoleRepository;

	@Autowired
	private AuthServiceClient authServiceClient;
	
	
	private String sendEmailId(String recipientEmail) throws SendFailedException {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
			helper.setFrom(SUPPLIER_EMAIL_CONTACT, MRMRSCART_SUPPORT);
			helper.setTo(recipientEmail);
			String subject = "MrMrsCart Admin Support";
			AdminRegistration findByEmail = adminRegistrationRepository.findByEmailIdAndStatus(recipientEmail,
					EAdminStatus.APPROVED);
			String password = LocalDateTime.now() + "," + recipientEmail + "," + findByEmail.getAdminRegistrationId();
			String encodedUrl = Base64.getUrlEncoder().encodeToString(password.getBytes());
			String content = "<p>Hello,</p>" + "<p>You are being assigned as an Admin Manager</p>"
					+ "<p>Please click the below link to set your credentials and proceed further</p>" + "Link: "
					+ "<a href = \"http://10.10.31.148:3010/auth/supplier/newpassword?user=" + encodedUrl
					+ "\">http://10.10.31.148:3010/auth/supplier/newpassword?user=" + encodedUrl + "</a>";
			helper.setSubject(subject);
			helper.setText(content, true);
			mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SendFailedException(SUPPLIER_MAIL_FAILED);
		}
		return SEND_MAIL;
	}
	
	
	@Override
	@Transactional
	public AdminRegistration addAdminManager(AdminManagerPojo data) {
		try {
			/* Phone number validation code starts */
			if (adminRegistrationRepository.findByMobileNumber(data.getMobileNumber()) != null) {
				throw new SupplierMobileNumberRegisteredException("Mobile Number Already Registered");
			}
			/* Phone number validation code ends */
			//Email id validation
			
			ResponseEntity<UserLoginResponse> userResponse = authServiceClient.getUserByEmailId(data.getEmailId());
			UserLoginResponse response = userResponse.getBody();
			if(response.getData()!=null) {
				throw new RoleException("Email Is Already Registered");
			}
			
			AdminRegistration adminRegistration = new AdminRegistration();
			BeanUtils.copyProperties(data, adminRegistration);
			
			adminRegistration.setDesignation(data.getDesignation().name());
			adminRegistration.setStatus(EAdminStatus.APPROVED);

			AdminCapability capability = new AdminCapability();
			capability.setAdminCapabilityList(data.getAdminCapabilities().getAdminCapabilityList());
			adminRegistration.setAdminCapabilities(capability);

			AdminRole role = adminRoleRepository.findByAdminRoleTitleIgnoreCase(EDesignation.ADMIN_MANAGER.name());
			List<AdminRole> adminRole = new ArrayList<>();
			adminRole.add(role);
			adminRegistration.setAdminRoles(adminRole);
			AdminRegistration registration = adminRegistrationRepository.save(adminRegistration);	
			sendEmailId(registration.getEmailId());
			return registration;
		}catch(SupplierMobileNumberRegisteredException | RoleException | FeignClientException e) { 
			throw e;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new AdminRegistrationException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public AdminRegistration getAdminManager(String id) {
		try {
			AdminRegistration adminManager = adminRegistrationRepository
					.findByAdminRegistrationIdAndStatusAndIsDelete(id, EAdminStatus.APPROVED, false);
			if (adminManager != null) {
				return adminManager;
			} else {
				throw new AdminManagerNotFound(INVALID_ADMIN_MANAGER);
			}
		} catch (AdminManagerNotFound e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AdminRegistrationException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public AdminRegistration updateAdminManager(AdminManagerPojo data) {
		try {
			AdminRegistration adminManager = adminRegistrationRepository.findByAdminRegistrationIdAndStatusAndIsDelete(
					data.getAdminRegistrationId(), EAdminStatus.APPROVED, false);
			if (adminManager != null) {
				/* Field can be updated first name,last name, */
				adminManager.setFirstName(data.getFirstName());
				adminManager.setLastName(data.getLastName());
				adminManager.setMobileNumber(data.getMobileNumber());
				adminManager.setDob(data.getDob());
				AdminCapability updatedCapability = adminManager.getAdminCapabilities();
				updatedCapability.setAdminCapabilityList(data.getAdminCapabilities().getAdminCapabilityList());
				adminManager.setAdminCapabilities(updatedCapability);
				return adminManager;
			} else {
				throw new AdminManagerNotFound(INVALID_ADMIN_MANAGER);
			}
		} catch (AdminManagerNotFound e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AdminRegistrationException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public boolean deleteAdminManager(String id) {
		try {
			AdminRegistration adminManager = adminRegistrationRepository
					.findByAdminRegistrationIdAndStatusAndIsDelete(id, EAdminStatus.APPROVED, false);
			if (adminManager != null) {
				adminManager.setDelete(true);
				adminRegistrationRepository.save(adminManager);
				return true;
			} else {
				throw new AdminManagerNotFound(INVALID_ADMIN_MANAGER);
			}
		} catch (AdminManagerNotFound e) {
			throw e;
		} catch (Exception e) {
			throw new AdminRegistrationException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public boolean disableAdminManager(String id) {
		try {
			AdminRegistration adminManager = adminRegistrationRepository
					.findByAdminRegistrationIdAndStatusAndIsDelete(id, EAdminStatus.APPROVED, false);
			if (adminManager != null) {
//				if(adminManager.getStatus() == 
				adminManager.setStatus(EAdminStatus.DISABLED);
				adminRegistrationRepository.save(adminManager);
				return true;
			} else {
				throw new AdminManagerNotFound(INVALID_ADMIN_MANAGER);
			}
		} catch (AdminManagerNotFound e) {
			throw e;
		} catch (Exception e) {
			throw new AdminRegistrationException(SOMETHING_WENT_WRONG);
		}
	}


	@Override
	public List<AdminRegistration> getFilteredAndPaginatedData(AdminManagerFilterPojo data) {
		/*
		 * Steps need to be follow 1. Fetch all the admin manager based on
		 * filter(status,createdByName) data with pagination 2.
		 */

		return null;
	}

}
