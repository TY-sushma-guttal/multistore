package com.mrmrscart.authservice.controller.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

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
import com.mrmrscart.authservice.feign.pojo.AuthenticationOtpPojo;
import com.mrmrscart.authservice.feign.pojo.StaffManagementInfo;
import com.mrmrscart.authservice.pojo.user.AdminRegistrationPojo;
import com.mrmrscart.authservice.pojo.user.UserLoginRequestPojo;
import com.mrmrscart.authservice.response.user.AuthenticateOtpResponse;
import com.mrmrscart.authservice.response.user.AuthenticateResponse;
import com.mrmrscart.authservice.service.user.AuthenticationService;
import com.mrmrscart.authservice.service.user.UserLoginService;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class AuthenticationControllerTest {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	private ObjectMapper mapper = new ObjectMapper();

	@MockBean
	private AuthenticationService authenticationService;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	void createAuthenticationTokenTest() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		AuthenticateResponse authenticateResponse = new AuthenticateResponse(false, "Login Successful !", "qwerty",
				"qwerty", new StaffManagementInfo(), new AdminRegistrationPojo());
		UserLoginRequestPojo loginRequestPojo = new UserLoginRequestPojo("test", "qwerty@123", EUserRole.ADMIN);
		when(authenticationService.createAuthenticationToken(Mockito.any())).thenReturn(authenticateResponse);
		String contentAsString = mockMvc
				.perform(post("/api/v1/auth/authenticate").accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(mapper.writeValueAsString(loginRequestPojo)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		AuthenticateResponse readValue = mapper.readValue(contentAsString, AuthenticateResponse.class);
		assertEquals("Login Successful !", readValue.getMessage());
	}

	@Test
	void createOtpAuthenticationTokenTest() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		AuthenticationOtpPojo authenticationOtpPojo = new AuthenticationOtpPojo("test", EUserRole.ADMIN, "qwerty@123");
		AuthenticateOtpResponse authenticateOtpResponse = new AuthenticateOtpResponse(false, "Login Successful !",
				"qwerty", "qwerty", false);
		when(authenticationService.createOtpAuthenticationToken(Mockito.any())).thenReturn(authenticateOtpResponse);
		String contentAsString = mockMvc
				.perform(post("/api/v1/auth/otp-authenticate").accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(mapper.writeValueAsString(authenticationOtpPojo)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		AuthenticateOtpResponse readValue = mapper.readValue(contentAsString, AuthenticateOtpResponse.class);
		assertEquals("Login Successful !", readValue.getMessage());
	}
}
