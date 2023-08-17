package com.mrmrscart.authservice.entity.user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

class UserLoginTest {

	private ObjectMapper mapper = new ObjectMapper();

	String json = "{\"userLoginId\":1,\"emailId\":\"supplier@gmail.com\",\"password\":\"password\",\"refreshToken\":\"refreeshToken\",\"userRoles\":[]}";

	@Test
	void serializationTest() throws Exception {
		UserLogin userLogin = new UserLogin(1, "supplier@gmail.com", "password", "refreeshToken", new ArrayList<>());
		String expected = mapper.writeValueAsString(mapper.readValue(json, UserLogin.class));
		assertEquals(mapper.writeValueAsString(userLogin), expected);
	}// end of serializationTest method

	@Test
	void deSerializeTest() throws JsonMappingException, JsonProcessingException {
		UserLogin userLogin = mapper.readValue(json, UserLogin.class);
		assertEquals("supplier@gmail.com", userLogin.getEmailId());
	}// end of deSerializeTest method
}
