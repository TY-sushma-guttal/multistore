package com.mrmrscart.authservice.service.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mrmrscart.authservice.entity.user.EUserRole;
import com.mrmrscart.authservice.entity.user.UserLogin;
import com.mrmrscart.authservice.exception.user.UserNameNotFoundException;
import com.mrmrscart.authservice.pojo.user.UserRolePojo;
import com.mrmrscart.authservice.repository.user.UserLoginRepository;

@ExtendWith(MockitoExtension.class)
class UserRoleServiceImplTest {

	@InjectMocks
	private UserRoleServiceImpl userRoleServiceImpl;

	@Mock
	private UserLoginRepository userLoginRepository;

	@Test
	void addUserRole() {
		UserRolePojo data = new UserRolePojo();
		data.setEmailId("test@gmail.com");
		data.setRole(EUserRole.ADMIN);
		UserLogin user = new UserLogin(1, "test@gmail.com", "test", "RefreshToken", new ArrayList<>());

		when(userLoginRepository.findByEmailId(Mockito.any())).thenReturn(user);
		when(userLoginRepository.save(Mockito.any())).thenReturn(user);

		UserLogin addedRoleDetails = userRoleServiceImpl.addUserRole(data);
		assertThat(addedRoleDetails.getUserRoles().get(0).getRole()).isEqualTo(EUserRole.ADMIN);
	}

	@Test
	void addUserRoleWithUserNameNotFoundException() {
		UserRolePojo data = new UserRolePojo();
		data.setEmailId("test@gmail.com");
		data.setRole(EUserRole.ADMIN);

		when(userLoginRepository.findByEmailId(Mockito.any())).thenReturn(null);
		assertThatThrownBy(() -> userRoleServiceImpl.addUserRole(data)).isInstanceOf(UserNameNotFoundException.class);
	}
	
	@Test
	void addUserRoleWithException() {
		UserRolePojo data = new UserRolePojo();
		data.setEmailId("test@gmail.com");
		data.setRole(EUserRole.ADMIN);

		when(userLoginRepository.findByEmailId(Mockito.any())).thenThrow();
		assertThatThrownBy(() -> userRoleServiceImpl.addUserRole(data)).isInstanceOf(Exception.class);
	}

}
