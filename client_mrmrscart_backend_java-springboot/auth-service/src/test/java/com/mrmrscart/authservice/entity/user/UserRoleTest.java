package com.mrmrscart.authservice.entity.user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

class UserRoleTest {
	
	ObjectMapper mapper = new ObjectMapper();
	
	String json = "{\"userRoleId\":1,\"role\":\"ADMIN\"}";
	
	@Test
	void serializeTest() throws JsonProcessingException {
		UserRole userRole = new UserRole(1, EUserRole.ADMIN);
		
		String expected = mapper.writeValueAsString(mapper.readValue(json, UserRole.class));
		assertEquals(mapper.writeValueAsString(userRole), expected);
	}
	
	@Test
	void deSerializeTest() throws JsonMappingException, JsonProcessingException {
		UserRole userRole = mapper.readValue(json, UserRole.class);
		assertEquals(1, userRole.getUserRoleId());
	}// end of deSerializeTest method

}
