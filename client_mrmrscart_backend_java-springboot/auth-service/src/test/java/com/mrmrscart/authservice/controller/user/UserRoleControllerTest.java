package com.mrmrscart.authservice.controller.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import com.mrmrscart.authservice.pojo.user.UserRolePojo;
import com.mrmrscart.authservice.response.user.SuccessResponse;
import com.mrmrscart.authservice.service.user.UserRoleService;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserRoleControllerTest {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	private ObjectMapper mapper = new ObjectMapper();

	@MockBean
	private UserRoleService userRoleService;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	void addUserRole() throws JsonProcessingException, UnsupportedEncodingException, Exception {
		UserLogin userLogin = new UserLogin(1, "test@gmail.com", "qwerty@123", "qwerty", new ArrayList<UserRole>());
		UserRolePojo userRolePojo = new UserRolePojo("test@gmail.com", EUserRole.ADMIN);
		when(userRoleService.addUserRole(Mockito.any())).thenReturn(userLogin);
		String contentAsString = mockMvc
				.perform(post("/api/v1/auth/user-role").accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE).content(mapper.writeValueAsString(userRolePojo)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		SuccessResponse readValue = mapper.readValue(contentAsString, SuccessResponse.class);
		assertEquals("User role added successfully. ", readValue.getMessage());
	}

}
