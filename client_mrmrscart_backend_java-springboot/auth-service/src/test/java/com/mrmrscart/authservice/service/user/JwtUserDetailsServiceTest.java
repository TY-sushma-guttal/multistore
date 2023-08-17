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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.mrmrscart.authservice.entity.user.EUserRole;
import com.mrmrscart.authservice.entity.user.UserLogin;
import com.mrmrscart.authservice.entity.user.UserRole;
import com.mrmrscart.authservice.repository.user.UserLoginRepository;

@ExtendWith(MockitoExtension.class)
class JwtUserDetailsServiceTest {

	@InjectMocks
	private JwtUserDetailsService jwtUserDetailsService;

	@Mock
	private UserLoginRepository userLoginRepository;

	@Test
	void loadUserByUsername() {
		List<UserRole> userRoles = new ArrayList<>();
		UserRole role = new UserRole(1, EUserRole.SUPPLIER);
		userRoles.add(role);

		UserLogin userLogin = new UserLogin(1, "supplier@gmail.com", "password", "refreeshToken", userRoles);

		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + userLogin.getUserRoles().get(0));
		authorities.add(authority);

		when(userLoginRepository.findByEmailId(Mockito.anyString())).thenReturn(userLogin);

		UserDetails userDetails = jwtUserDetailsService.loadUserByUsername("supplier@gmail.com");
		assertThat(userDetails.getPassword()).isEqualTo("password");
	}// End of loadUserByUsername method
	
	@Test
	void loadUserByUsernameWithUserLoginNull() {
		when(userLoginRepository.findByEmailId(Mockito.anyString())).thenReturn(null);

		assertThatThrownBy(() -> jwtUserDetailsService.loadUserByUsername("supplier@gmail.com"))
		.isInstanceOf(UsernameNotFoundException.class);
	}// End of loadUserByUsernameWithUserLoginNull method

	@Test
	void loadUserByUsernameWithPasswordNull() {

		UserLogin userLogin = new UserLogin(1, "supplier@gmail.com", null, "refreeshToken", new ArrayList<>());

		when(userLoginRepository.findByEmailId(Mockito.anyString())).thenReturn(userLogin);

		assertThatThrownBy(() -> jwtUserDetailsService.loadUserByUsername("supplier@gmail.com"))
		.isInstanceOf(UsernameNotFoundException.class);
	}// End of loadUserByUsernameWithPasswordNull method


}
