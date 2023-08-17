package com.mrmrscart.userservice.service.admin;

import static com.mrmrscart.userservice.common.admin.AdminRegistrationConstant.INVALID_ADMIN_USER;
import static com.mrmrscart.userservice.common.admin.AdminRegistrationConstant.SOMETHING_WENT_WRONG;
import static com.mrmrscart.userservice.common.supplier.SupplierConstant.MRMRSCART_SUPPORT;
import static com.mrmrscart.userservice.common.supplier.SupplierConstant.SEND_MAIL;
import static com.mrmrscart.userservice.common.supplier.SupplierConstant.SUPPLIER_EMAIL_CONTACT;
import static com.mrmrscart.userservice.common.supplier.SupplierConstant.SUPPLIER_MAIL_FAILED;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.mail.SendFailedException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.mrmrscart.userservice.entity.admin.AdminCapability;
import com.mrmrscart.userservice.entity.admin.AdminRegistration;
import com.mrmrscart.userservice.entity.admin.AdminRole;
import com.mrmrscart.userservice.entity.admin.EAdminStatus;
import com.mrmrscart.userservice.exception.admin.AdminException;
import com.mrmrscart.userservice.exception.admin.AdminNotFoundException;
import com.mrmrscart.userservice.pojo.admin.AdminUserFilterPojo;
import com.mrmrscart.userservice.pojo.admin.AdminUserPojo;
import com.mrmrscart.userservice.pojo.customer.DropDownPojo;
import com.mrmrscart.userservice.repository.admin.AdminRegistrationRepository;
import com.mrmrscart.userservice.repository.admin.AdminRoleRepository;
import com.mrmrscart.userservice.util.PaginatedResponse;

@Service
public class AdminUserServiceImpl implements AdminUserService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private AdminRegistrationRepository adminRegistrationRepository;

	@Autowired
	private AdminRoleRepository adminRoleRepository;

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

			String content = "<p>Hello,</p>" + "<p>You are being assigned as an Admin User</p>"
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
	public AdminRegistration createAdminUser(AdminUserPojo adminUserPojo) throws SendFailedException {
		try {
			saveHelper(adminUserPojo);
			AdminRegistration adminRegistration = new AdminRegistration();
			AdminCapability adminCapability = new AdminCapability();
			AdminRole adminRole = new AdminRole();
			List<AdminRole> adminRoles = new ArrayList<>();
			adminCapability.setAdminCapabilityList(adminUserPojo.getAdminCapabilityList());
			BeanUtils.copyProperties(adminUserPojo, adminRegistration);
			adminRegistration.setStatus(EAdminStatus.APPROVED);
			adminRegistration.setDesignation(adminUserPojo.getDesignation());
			AdminRole role = adminRoleRepository.findByAdminRoleTitleIgnoreCase("ADMIN_USER");
			if (role != null) {
				adminRoles.add(role);
			} else {
				adminRole.setAdminRoleTitle("ADMIN_USER");
				adminRoles.add(adminRole);
			}
			adminRegistration.setAdminRoles(adminRoles);
			adminRegistration.setAdminCapabilities(adminCapability);
			AdminRegistration registration = adminRegistrationRepository.save(adminRegistration);

			sendEmailId(registration.getEmailId());
			return registration;
		} catch (Exception e) {
			throw new AdminException(SOMETHING_WENT_WRONG);
		}
	}

	private void saveHelper(AdminUserPojo adminUserPojo) {

		adminRegistrationRepository.findAll().forEach(user -> {
			if (user.getEmailId().equalsIgnoreCase(adminUserPojo.getEmailId())
					|| user.getMobileNumber().equals(adminUserPojo.getMobileNumber())) {
				throw new AdminException("EmailId/Mobile Number Already Exists!!!!");
			}
		});

	}

	@Override
	@Transactional
	public AdminRegistration updateAdminUser(AdminUserPojo adminUserPojo) {
		try {
			saveHelper(adminUserPojo);
			AdminRegistration adminRegistration = adminRegistrationRepository
					.findByAdminRegistrationIdAndIsDelete(adminUserPojo.getAdminRegistrationId(), false);
			if (adminRegistration != null) {
				BeanUtils.copyProperties(adminUserPojo, adminRegistration, "adminRegistrationId");
				AdminCapability adminCapabilities = adminRegistration.getAdminCapabilities();
				adminCapabilities.setAdminCapabilityList(adminUserPojo.getAdminCapabilityList());
				adminRegistration.setAdminCapabilities(adminCapabilities);
				return adminRegistrationRepository.save(adminRegistration);
			} else {
				throw new AdminNotFoundException(INVALID_ADMIN_USER);
			}
		} catch (Exception e) {
			throw new AdminException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public AdminRegistration disableAdminUser(String adminRegistrationId, String status) {
		try {
			AdminRegistration adminRegistration = adminRegistrationRepository
					.findByAdminRegistrationIdAndIsDelete(adminRegistrationId, false);
			if (adminRegistration != null) {
				adminRegistration.setStatus(EAdminStatus.valueOf(status));
				return adminRegistrationRepository.save(adminRegistration);
			} else {
				throw new AdminNotFoundException(INVALID_ADMIN_USER);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new AdminException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public boolean deleteAdminUser(String adminRegistrationId) {
		try {
			AdminRegistration adminRegistration = adminRegistrationRepository
					.findByAdminRegistrationIdAndIsDelete(adminRegistrationId, false);
			if (adminRegistration != null) {
				adminRegistration.setDelete(true);
				adminRegistrationRepository.save(adminRegistration);
				return true;
			} else {
				throw new AdminNotFoundException(INVALID_ADMIN_USER);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new AdminException(SOMETHING_WENT_WRONG);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AdminRegistration> getAllAdminUsers(int pageNumber, int pageSize,
			AdminUserFilterPojo adminUserFilterPojo) {

		List<AdminRegistration> find = adminRegistrationRepository.findByIsDelete(false);
		if (!adminUserFilterPojo.getStatus().isEmpty()) {
			find = find.stream().filter(a -> adminUserFilterPojo.getStatus().contains(a.getStatus().name()))
					.collect(Collectors.toList());
		}
		if (!adminUserFilterPojo.getCreatedBy().isEmpty()) {
			find = find.stream().filter(a -> adminUserFilterPojo.getCreatedBy().contains(a.getCreatedBy()))
					.collect(Collectors.toList());
		}
		if (adminUserFilterPojo.getKeyword() != null) {
			find = find.stream()
					.filter(user -> user.getFirstName().contains(adminUserFilterPojo.getKeyword())
							|| user.getLastName().contains(adminUserFilterPojo.getKeyword())
							|| user.getEmailId().contains(adminUserFilterPojo.getKeyword())
							|| user.getMobileNumber().contains(adminUserFilterPojo.getKeyword()))
					.collect(Collectors.toList());
		}

		if (adminUserFilterPojo.getDateFrom() != null && adminUserFilterPojo.getDateTo() != null) {
			find = find.stream()
					.filter(f -> adminUserFilterPojo.getDateFrom().isBefore(f.getCreatedDate())
							&& adminUserFilterPojo.getDateTo().isAfter(f.getCreatedDate()))
					.collect(Collectors.toList());
			if (find.isEmpty())
				return find;
		}
		return (List<AdminRegistration>) PaginatedResponse.getPaginatedResponse(find, pageNumber, pageSize);
	}

	@Override
	public List<EAdminStatus> getAllStatus() {
		return Arrays.asList(EAdminStatus.values());
	}

	@Override
	public List<DropDownPojo> getAllCreatedBy() {
		try {
			List<DropDownPojo> downPojos = new ArrayList<>();
			List<String> collect;
			List<AdminRegistration> findAll = adminRegistrationRepository.findAll();
			collect = findAll.stream().map(AdminRegistration::getCreatedBy).collect(Collectors.toList());
			Set<String> createdBy = new LinkedHashSet<>(collect);
			if (!createdBy.isEmpty()) {
				createdBy.forEach(id -> {
					AdminRegistration adminRegistration = adminRegistrationRepository
							.findByAdminRegistrationIdAndIsDelete(id, false);
					if (adminRegistration != null) {
						DropDownPojo downPojo = new DropDownPojo(adminRegistration.getAdminRegistrationId(),
								adminRegistration.getFirstName() + " " + adminRegistration.getLastName());
						downPojos.add(downPojo);
					}
				});

			}
			return downPojos;
		} catch (Exception e) {
			throw new AdminException(SOMETHING_WENT_WRONG);
		}
	}

}
