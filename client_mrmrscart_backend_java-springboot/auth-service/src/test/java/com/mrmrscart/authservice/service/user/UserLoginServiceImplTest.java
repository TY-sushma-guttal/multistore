package com.mrmrscart.authservice.service.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mrmrscart.authservice.entity.user.EUserRole;
import com.mrmrscart.authservice.entity.user.UserLogin;
import com.mrmrscart.authservice.entity.user.UserRole;
import com.mrmrscart.authservice.exception.user.PasswordNotMatchedException;
import com.mrmrscart.authservice.exception.user.UserException;
import com.mrmrscart.authservice.pojo.user.ChangePasswordPojo;
import com.mrmrscart.authservice.pojo.user.CustomerPojo;
import com.mrmrscart.authservice.pojo.user.ForgotPasswordPojo;
import com.mrmrscart.authservice.pojo.user.UserLoginPojo;
import com.mrmrscart.authservice.repository.user.UserLoginRepository;

@ExtendWith(MockitoExtension.class)
class UserLoginServiceImplTest {

	@InjectMocks
	private UserLoginServiceImpl userLoginServiceImpl;

	@Mock
	private UserLoginRepository userLoginRepository;

	@Mock
	private PasswordEncoder bcryptEncoder;

	@Test
	void getUserByEmailId() {
		UserLogin user = new UserLogin(1, "test@gmail.com", "test", "RefreshToken", new ArrayList<>());

		when(userLoginRepository.findByEmailId(Mockito.any())).thenReturn(user);

		UserLogin requestedUser = userLoginServiceImpl.getUserByEmailId("test@gmail.com");
		assertThat(requestedUser.getEmailId()).isEqualTo("test@gmail.com");
	}

	@Test
	void addCustomerIfEmailExists() {
		CustomerPojo customerPojo = new CustomerPojo("test@gmail.com", "test");
		UserLogin user = new UserLogin(1, "test@gmail.com", "test", "RefreshToken", new ArrayList<>());

		when(userLoginRepository.findByEmailId(Mockito.any())).thenReturn(user);
		when(userLoginRepository.save(Mockito.any())).thenReturn(user);

		UserLogin addedCustomer = userLoginServiceImpl.addCustomer(customerPojo);
		assertThat(addedCustomer.getUserRoles().get(0).getRole()).isEqualTo(EUserRole.CUSTOMER);
	}

	@Test
	void addCustomer() {
		CustomerPojo customerPojo = new CustomerPojo("test1@gmail.com", "test");
		UserLogin user = new UserLogin(1, "test@gmail.com", "test", "RefreshToken", new ArrayList<>());

		when(userLoginRepository.findByEmailId(Mockito.any())).thenReturn(null);
		when(bcryptEncoder.encode(Mockito.anyString())).thenReturn("test");
		when(userLoginRepository.save(Mockito.any())).thenReturn(user);

		UserLogin addedCustomer = userLoginServiceImpl.addCustomer(customerPojo);
		assertThat(addedCustomer.getPassword()).isEqualTo("test");
	}

	@Test
	void addCustomerIfNoPassword() {
		CustomerPojo customerPojo = new CustomerPojo("test@gmail.com", "test");
		UserLogin user = new UserLogin(1, "test@gmail.com", null, "RefreshToken", new ArrayList<>());

		when(userLoginRepository.findByEmailId(Mockito.any())).thenReturn(user);
		when(bcryptEncoder.encode(Mockito.anyString())).thenReturn("test");
		when(userLoginRepository.save(Mockito.any())).thenReturn(user);

		UserLogin addedCustomer = userLoginServiceImpl.addCustomer(customerPojo);
		assertThat(addedCustomer.getPassword()).isEqualTo("test");
	}

	@Test
	void addUserEmail() {
		UserLogin user = new UserLogin(1, "test@gmail.com", "test", "RefreshToken", new ArrayList<>());

		when(userLoginRepository.save(Mockito.any())).thenReturn(user);

		UserLogin addedUser = userLoginServiceImpl.addUserEmail("test@gmail.com");
		assertThat(addedUser.getEmailId()).isEqualTo("test@gmail.com");
	}
	
	@Test
	void addCustomerWithException() {
		CustomerPojo customerPojo = new CustomerPojo("test@gmail.com", "test");
		
		when(userLoginRepository.findByEmailId(Mockito.any())).thenThrow(UserException.class);
		assertThatThrownBy(() -> userLoginServiceImpl.addCustomer(customerPojo)).isInstanceOf(UserException.class);
	}

	@Test
	void resetPassword() {
		List<UserRole> userRoles = new ArrayList<>();
		UserRole role = new UserRole(1, EUserRole.SUPPLIER);
		userRoles.add(role);
		UserLogin userLogin = new UserLogin(1, "supplier@gmail.com", "password", "refreeshToken", userRoles);
		ForgotPasswordPojo forgotPasswordPojo = new ForgotPasswordPojo("supplierName", "password", "password");

		when(userLoginRepository.findByEmailId(Mockito.anyString())).thenReturn(userLogin);
		when(bcryptEncoder.encode(Mockito.anyString())).thenReturn("password");
		when(userLoginRepository.save(Mockito.any())).thenReturn(userLogin);

		UserLogin result = userLoginServiceImpl.resetPassword(forgotPasswordPojo);
		assertThat(result.getPassword()).isEqualTo("password");
	}// End of resetPassword method

	@Test
	void changePassword() {
		List<UserRole> userRoles = new ArrayList<>();
		UserRole role = new UserRole(1, EUserRole.SUPPLIER);
		userRoles.add(role);
		UserLogin userLogin = new UserLogin(1, "supplier@gmail.com", "password", "refreeshToken", userRoles);
		ChangePasswordPojo changePasswordPojo = new ChangePasswordPojo("supplier@gmail.com", "password", "password",
				"password", EUserRole.SUPPLIER);

		when(userLoginRepository.findByEmailId(Mockito.anyString())).thenReturn(userLogin);
		when(bcryptEncoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		when(bcryptEncoder.encode(Mockito.anyString())).thenReturn("password");
		when(userLoginRepository.save(Mockito.any())).thenReturn(userLogin);

		UserLogin result = userLoginServiceImpl.changePassword(changePasswordPojo);
		assertThat(result.getPassword()).isEqualTo("password");
	}// End of changePassword method

	@Test
	void changePasswordWithPasswordNotMatchedException() {
		List<UserRole> userRoles = new ArrayList<>();
		UserRole role = new UserRole(1, EUserRole.SUPPLIER);
		userRoles.add(role);
		UserLogin userLogin = new UserLogin(1, "supplier@gmail.com", "password", "refreeshToken", userRoles);
		ChangePasswordPojo changePasswordPojo = new ChangePasswordPojo("supplier@gmail.com", "password", "password",
				"password", EUserRole.SUPPLIER);

		when(userLoginRepository.findByEmailId(Mockito.anyString())).thenReturn(userLogin);
		when(bcryptEncoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(false);

		assertThatThrownBy(() -> userLoginServiceImpl.changePassword(changePasswordPojo))
				.isInstanceOf(PasswordNotMatchedException.class);
	}// End of changePasswordWithPasswordNotMatchedException method

	
	@Test
	void changePasswordWithUserException() {
		ChangePasswordPojo changePasswordPojo = new ChangePasswordPojo("supplier@gmail.com", "password", "password",
				"password", EUserRole.SUPPLIER);

		when(userLoginRepository.findByEmailId(Mockito.anyString())).thenThrow(UserException.class);
		assertThatThrownBy(() -> userLoginServiceImpl.changePassword(changePasswordPojo))
				.isInstanceOf(UserException.class);
	}// End of changePasswordWithUserException method
	
	@Test
	void addUser() {
		UserLoginPojo userLoginPojo = new UserLoginPojo(1, "supplier@gmail.com", "password", "refreeshToken", new ArrayList<>());
		UserLogin userLogin = new UserLogin(1, "supplier@gmail.com", "password", "refreeshToken", new ArrayList<>());
		
		when(userLoginRepository.save(Mockito.any())).thenReturn(userLogin);
		
		UserLogin result = userLoginServiceImpl.addUser(userLoginPojo);
		assertThat(result.getPassword()).isEqualTo("password");
	}// End of resetPassword method


}
