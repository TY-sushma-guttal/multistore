package com.mrmrscart.authservice.controller.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrmrscart.authservice.entity.user.EUserRole;
import com.mrmrscart.authservice.entity.user.UserLogin;
import com.mrmrscart.authservice.entity.user.UserRole;
import com.mrmrscart.authservice.pojo.user.ChangePasswordPojo;
import com.mrmrscart.authservice.pojo.user.CustomerPojo;
import com.mrmrscart.authservice.pojo.user.ForgotPasswordPojo;
import com.mrmrscart.authservice.pojo.user.UserLoginPojo;
import com.mrmrscart.authservice.response.user.SuccessResponse;
import com.mrmrscart.authservice.response.user.UserLoginResponse;
import com.mrmrscart.authservice.service.user.UserLoginService;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserLoginControllerTest {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	private ObjectMapper mapper = new ObjectMapper();

	@MockBean
	private UserLoginService userLoginService;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	void getUserByEmailIdTest() throws UnsupportedEncodingException, Exception {
		UserLogin userLogin = new UserLogin(1, "test@gmail.com", "qwerty@123", "qwerty", new ArrayList<UserRole>());
		when(userLoginService.getUserByEmailId(Mockito.anyString())).thenReturn(userLogin);
		String contentAsString = mockMvc
				.perform(get("/api/v1/auth/user-email/hi@gmail.com").accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		UserLoginResponse readValue = mapper.readValue(contentAsString, UserLoginResponse.class);
		assertEquals("Email Fetched Successfully. ", readValue.getMessage());
	}

	@Test
	void addUserEmailTest() throws UnsupportedEncodingException, Exception {
		UserLogin userLogin = new UserLogin(1, "test@gmail.com", "qwerty@123", "qwerty", new ArrayList<UserRole>());
		when(userLoginService.addUserEmail(Mockito.anyString())).thenReturn(userLogin);
		String contentAsString = mockMvc
				.perform(post("/api/v1/auth/user-email/hi@gmail.com").accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		UserLoginResponse readValue = mapper.readValue(contentAsString, UserLoginResponse.class);
		assertEquals("Email Fetched Successfully. ", readValue.getMessage());
	}

	@Test
	void resetPasswordTest() throws UnsupportedEncodingException, Exception {
		ForgotPasswordPojo forgotPasswordPojo = new ForgotPasswordPojo("test@gmail.com", "qwerty@123", "qwerty@12345");
		UserLogin userLogin = new UserLogin(1, "test@gmail.com", "qwerty@123", "qwerty", new ArrayList<UserRole>());
		when(userLoginService.resetPassword(Mockito.any())).thenReturn(userLogin);
		String contentAsString = mockMvc
				.perform(put("/api/v1/auth/user/reset-password").accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(mapper.writeValueAsString(forgotPasswordPojo)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		UserLoginResponse readValue = mapper.readValue(contentAsString, UserLoginResponse.class);
		assertEquals("Password reset successful!", readValue.getMessage());
	}

	@Test
	void changePasswordTest() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		ChangePasswordPojo changePasswordPojo = new ChangePasswordPojo("test@gmail.com", "qwerty@123", "qwerty@12345",
				"qwerty@12345", EUserRole.ADMIN);
		UserLogin userLogin = new UserLogin(1, "test@gmail.com", "qwerty@123", "qwerty", new ArrayList<UserRole>());
		when(userLoginService.changePassword(Mockito.any())).thenReturn(userLogin);
		String contentAsString = mockMvc
				.perform(put("/api/v1/auth/user/change-password").accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(mapper.writeValueAsString(changePasswordPojo)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		UserLoginResponse readValue = mapper.readValue(contentAsString, UserLoginResponse.class);
		assertEquals("Password update successful!", readValue.getMessage());
	}

	@Test
	void addUserTest() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		UserLoginPojo loginPojo = new UserLoginPojo(1, "test@gmail.com", "qwerty@123", "qwerty",
				new ArrayList<UserRole>());
		UserLogin userLogin = new UserLogin(1, "test@gmail.com", "qwerty@123", "qwerty", new ArrayList<UserRole>());
		when(userLoginService.addUser(Mockito.any())).thenReturn(userLogin);
		String contentAsString = mockMvc
				.perform(post("/api/v1/auth/user").accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(loginPojo)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		SuccessResponse readValue = mapper.readValue(contentAsString, SuccessResponse.class);
		assertEquals("User login added successfully. ", readValue.getMessage());
	}

	@Test
	void addCustomerTest() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		CustomerPojo customerPojo = new CustomerPojo("test@gmail.com", "qwerty@123");
		UserLogin userLogin = new UserLogin(1, "test@gmail.com", "qwerty@123", "qwerty", new ArrayList<UserRole>());
		when(userLoginService.addCustomer(Mockito.any())).thenReturn(userLogin);
		String contentAsString = mockMvc
				.perform(post("/api/v1/auth/user/customer-registration").accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(customerPojo)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		SuccessResponse readValue = mapper.readValue(contentAsString, SuccessResponse.class);
		assertEquals("User login added successfully. ", readValue.getMessage());
	}
}
