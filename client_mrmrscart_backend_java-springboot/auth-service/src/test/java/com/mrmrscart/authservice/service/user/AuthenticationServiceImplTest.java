package com.mrmrscart.authservice.service.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.test.util.ReflectionTestUtils;

import com.mrmrscart.authservice.entity.user.EAdminStatus;
import com.mrmrscart.authservice.entity.user.EUserRole;
import com.mrmrscart.authservice.entity.user.UserLogin;
import com.mrmrscart.authservice.entity.user.UserRole;
import com.mrmrscart.authservice.exception.user.InvalidCredentailException;
import com.mrmrscart.authservice.exception.user.UnauthorizedException;
import com.mrmrscart.authservice.exception.user.UserNameNotFoundException;
import com.mrmrscart.authservice.feign.pojo.AuthenticationOtpPojo;
import com.mrmrscart.authservice.feign.pojo.StaffManagementInfo;
import com.mrmrscart.authservice.feign.response.EmailResponse;
import com.mrmrscart.authservice.feign.response.StaffManagementInfoResponse;
import com.mrmrscart.authservice.feign.user.UserServiceClient;
import com.mrmrscart.authservice.pojo.user.AdminRegistrationPojo;
import com.mrmrscart.authservice.pojo.user.UserLoginRequestPojo;
import com.mrmrscart.authservice.repository.user.UserLoginRepository;
import com.mrmrscart.authservice.response.user.AdminRegistrationResponse;
import com.mrmrscart.authservice.response.user.AuthenticateOtpResponse;
import com.mrmrscart.authservice.response.user.AuthenticateResponse;

import feign.FeignException;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

	@InjectMocks
	private AuthenticationServiceImpl authenticationServiceImpl;

	@Mock
	private UserServiceClient userServiceClient;

	@Mock
	private UserLoginRepository userLoginRepository;

	@Mock
	private AuthenticationManager authenticationManager;

	@BeforeEach
	void testValueSetup() {
		ReflectionTestUtils.setField(authenticationServiceImpl, "secret", "secret");
	}

	@Test
	void createOtpAuthenticationToken() {
		AuthenticationOtpPojo authenticationOtpPojo = new AuthenticationOtpPojo("test@gmail.com", EUserRole.ADMIN, "1");
		List<UserRole> userRoles = new ArrayList<>();
		UserRole userRole = new UserRole(1, EUserRole.ADMIN);
		userRoles.add(userRole);
		UserLogin user = new UserLogin(1, "test@gmail.com", "test", "RefreshToken", new ArrayList<>());

		when(userLoginRepository.findByEmailId(Mockito.anyString())).thenReturn(user);

		AuthenticateOtpResponse authenticateOtpResponse = authenticationServiceImpl
				.createOtpAuthenticationToken(authenticationOtpPojo);

		assertThat(authenticateOtpResponse.isStaff()).isFalse();
	}

	@Test
	void createOtpAuthenticationTokenWithStaff() {
		AuthenticationOtpPojo authenticationOtpPojo = new AuthenticationOtpPojo("test@gmail.com", EUserRole.STAFF, "1");
		List<UserRole> userRoles = new ArrayList<>();
		UserRole userRole = new UserRole(1, EUserRole.STAFF);
		userRoles.add(userRole);
		UserLogin user = new UserLogin(1, "test@gmail.com", "test", "RefreshToken", userRoles);

		when(userLoginRepository.findByEmailId(Mockito.anyString())).thenReturn(user);

		AuthenticateOtpResponse authenticateOtpResponse = authenticationServiceImpl
				.createOtpAuthenticationToken(authenticationOtpPojo);

		assertThat(authenticateOtpResponse.isStaff()).isTrue();
	}

	@Test
	void createAuthTokenForAdmin() {
		UserLoginRequestPojo userLoginRequestPojo = new UserLoginRequestPojo("admin", "admin@gmail.com",
				EUserRole.ADMIN);

		List<UserRole> userRoles = new ArrayList<>();
		UserRole role = new UserRole(1, EUserRole.ADMIN);
		userRoles.add(role);
		UserLogin userLogin = new UserLogin(1, "admin@gmail.com", "password", "refreeshToken", userRoles);

		ResponseEntity<EmailResponse> verifyUser = new ResponseEntity<EmailResponse>(
				new EmailResponse(false, "message", "admin@gmail.com", "userId"), HttpStatus.OK);

		AdminRegistrationPojo adminRegistrationPojo = new AdminRegistrationPojo("1", "admin@gmail.com", "admin", null,
				null, null, "9876543210", "admin", EAdminStatus.INITIATED, false, null, null);
		AdminRegistrationResponse adminRegistrationResponse = new AdminRegistrationResponse(false, "message",
				adminRegistrationPojo);
		ResponseEntity<AdminRegistrationResponse> adminInfo = new ResponseEntity<AdminRegistrationResponse>(
				adminRegistrationResponse, HttpStatus.OK);

		when(userServiceClient.verifyUser(Mockito.any())).thenReturn(verifyUser);
		when(userLoginRepository.findByEmailId(Mockito.any())).thenReturn(userLogin);
		when(userServiceClient.getAdminInfo(Mockito.any())).thenReturn(adminInfo);

		AuthenticateResponse response = authenticationServiceImpl.createAuthenticationToken(userLoginRequestPojo);
		assertThat(response.getAdminRegistrationPojo().getEmailId()).isEqualTo("admin@gmail.com");

	}
	
	@Test
	void createAuthTokenForAdminWithUserNameNotFoundException() {
		UserLoginRequestPojo userLoginRequestPojo = new UserLoginRequestPojo("admin", "admin@gmail.com",
				EUserRole.ADMIN);

		List<UserRole> userRoles = new ArrayList<>();
		UserRole role = new UserRole(1, EUserRole.ADMIN);
		userRoles.add(role);
		UserLogin userLogin = new UserLogin(1, "admin@gmail.com", "password", "refreeshToken", userRoles);

		ResponseEntity<EmailResponse> verifyUser = new ResponseEntity<EmailResponse>(
				new EmailResponse(false, "message", "admin@gmail.com", "userId"), HttpStatus.OK);
		
		AdminRegistrationResponse adminRegistrationResponse = null;
		ResponseEntity<AdminRegistrationResponse> adminInfo = new ResponseEntity<AdminRegistrationResponse>(
				adminRegistrationResponse, HttpStatus.BAD_REQUEST);

		when(userServiceClient.verifyUser(Mockito.any())).thenReturn(verifyUser);
		when(userLoginRepository.findByEmailId(Mockito.any())).thenReturn(userLogin);
		when(userServiceClient.getAdminInfo(Mockito.any())).thenReturn(adminInfo);
		
		assertThatThrownBy(() -> authenticationServiceImpl.createAuthenticationToken(userLoginRequestPojo)).isInstanceOf(UserNameNotFoundException.class);

	}
	
	@Test
	void createAuthTokenForAdminWithException() {
		UserLoginRequestPojo userLoginRequestPojo = new UserLoginRequestPojo("admin", "admin@gmail.com",
				EUserRole.ADMIN);

		List<UserRole> userRoles = new ArrayList<>();
		UserRole role = new UserRole(1, EUserRole.ADMIN);
		userRoles.add(role);
		UserLogin userLogin = new UserLogin(1, "admin@gmail.com", "password", "refreeshToken", userRoles);

		ResponseEntity<EmailResponse> verifyUser = new ResponseEntity<EmailResponse>(
				new EmailResponse(false, "message", "admin@gmail.com", "userId"), HttpStatus.OK);

		when(userServiceClient.verifyUser(Mockito.any())).thenReturn(verifyUser);
		when(userLoginRepository.findByEmailId(Mockito.any())).thenReturn(userLogin);
		when(userServiceClient.getAdminInfo(Mockito.any())).thenReturn(null);
		
		assertThatThrownBy(() -> authenticationServiceImpl.createAuthenticationToken(userLoginRequestPojo)).isInstanceOf(Exception.class);


	}


	@Test
	void createAuthTokenWithUserNameNotFoundException() {
		UserLoginRequestPojo userLoginRequestPojo = new UserLoginRequestPojo("name", "password", EUserRole.SUPPLIER);
		EmailResponse emailResponse = null;
		ResponseEntity<EmailResponse> verifyUser = new ResponseEntity<EmailResponse>(emailResponse, HttpStatus.OK);

		when(userServiceClient.verifyUser(Mockito.any())).thenReturn(verifyUser);

		assertThatThrownBy(() -> authenticationServiceImpl.createAuthenticationToken(userLoginRequestPojo))
				.isInstanceOf(UserNameNotFoundException.class);
	}

	@Test
	void createAuthTokenWithStaffManagement() {

		UserLoginRequestPojo userLoginRequestPojo = new UserLoginRequestPojo("name", "password", EUserRole.SUPPLIER);

		List<UserRole> userRoles = new ArrayList<>();
		UserRole role = new UserRole(1, EUserRole.STAFF);
		userRoles.add(role);

		UserLogin userLogin = new UserLogin(1, "supplier@gmail.com", "password", "refreeshToken", userRoles);

		ResponseEntity<EmailResponse> verifyUser = new ResponseEntity<EmailResponse>(
				new EmailResponse(false, "message", "supplier@gmail.com", "userId"), HttpStatus.OK);
		ResponseEntity<StaffManagementInfoResponse> staffManagementInfo = new ResponseEntity<StaffManagementInfoResponse>(
				new StaffManagementInfoResponse(false, "message", new StaffManagementInfo()), HttpStatus.OK);

		when(userServiceClient.verifyUser(Mockito.any())).thenReturn(verifyUser);
		when(userLoginRepository.findByEmailId(Mockito.any())).thenReturn(userLogin);
		when(userServiceClient.getStaffManagementInfo(Mockito.any())).thenReturn(staffManagementInfo);

		AuthenticateResponse result = authenticationServiceImpl.createAuthenticationToken(userLoginRequestPojo);
		assertThat(result.getMessage()).isEqualTo("Login Successful! ");
	}

	@Test
	void createAuthTokenWithoutStaffManagement() {

		UserLoginRequestPojo userLoginRequestPojo = new UserLoginRequestPojo("name", "password", EUserRole.SUPPLIER);

		List<UserRole> userRoles = new ArrayList<>();
		UserRole role = new UserRole(1, EUserRole.STAFF);
		userRoles.add(role);

		UserLogin userLogin = new UserLogin(1, "supplier@gmail.com", "password", "refreeshToken", userRoles);

		ResponseEntity<EmailResponse> verifyUser = new ResponseEntity<EmailResponse>(
				new EmailResponse(false, "message", "supplier@gmail.com", "userId"), HttpStatus.OK);

		StaffManagementInfoResponse infoResponse = null;
		ResponseEntity<StaffManagementInfoResponse> staffManagementInfo = new ResponseEntity<StaffManagementInfoResponse>(
				infoResponse, HttpStatus.OK);

		when(userServiceClient.verifyUser(Mockito.any())).thenReturn(verifyUser);
		when(userLoginRepository.findByEmailId(Mockito.any())).thenReturn(userLogin);
		when(userServiceClient.getStaffManagementInfo(Mockito.any())).thenReturn(staffManagementInfo);

		assertThatThrownBy(() -> authenticationServiceImpl.createAuthenticationToken(userLoginRequestPojo))
				.isInstanceOf(UserNameNotFoundException.class);
	}

	@Test
	void createAuthTokenWithFeignException() {
		UserLoginRequestPojo userLoginRequestPojo = new UserLoginRequestPojo("name", "password", EUserRole.SUPPLIER);

		when(userServiceClient.verifyUser(Mockito.any())).thenThrow(FeignException.class);

		assertThatThrownBy(() -> authenticationServiceImpl.createAuthenticationToken(userLoginRequestPojo))
				.isInstanceOf(FeignException.class);
	}

	@Test
	void createAuthTokenWithDisabledException() {
		UserLoginRequestPojo userLoginRequestPojo = new UserLoginRequestPojo("name", "password", EUserRole.SUPPLIER);

		List<UserRole> userRoles = new ArrayList<>();
		UserRole role = new UserRole(1, EUserRole.STAFF);
		userRoles.add(role);

		ResponseEntity<EmailResponse> verifyUser = new ResponseEntity<EmailResponse>(
				new EmailResponse(false, "message", "supplier@gmail.com", "userId"), HttpStatus.OK);

		when(userServiceClient.verifyUser(Mockito.any())).thenReturn(verifyUser);
		when(authenticationManager.authenticate(Mockito.any())).thenThrow(DisabledException.class);

		assertThatThrownBy(() -> authenticationServiceImpl.createAuthenticationToken(userLoginRequestPojo))
				.isInstanceOf(UnauthorizedException.class);
	}

	@Test
	void createAuthTokenWithBadCredentialsException() {
		UserLoginRequestPojo userLoginRequestPojo = new UserLoginRequestPojo("name", "password", EUserRole.SUPPLIER);

		List<UserRole> userRoles = new ArrayList<>();
		UserRole role = new UserRole(1, EUserRole.STAFF);
		userRoles.add(role);

		ResponseEntity<EmailResponse> verifyUser = new ResponseEntity<EmailResponse>(
				new EmailResponse(false, "message", "supplier@gmail.com", "userId"), HttpStatus.OK);

		when(userServiceClient.verifyUser(Mockito.any())).thenReturn(verifyUser);
		when(authenticationManager.authenticate(Mockito.any())).thenThrow(BadCredentialsException.class);

		assertThatThrownBy(() -> authenticationServiceImpl.createAuthenticationToken(userLoginRequestPojo))
				.isInstanceOf(InvalidCredentailException.class);
	}

}
