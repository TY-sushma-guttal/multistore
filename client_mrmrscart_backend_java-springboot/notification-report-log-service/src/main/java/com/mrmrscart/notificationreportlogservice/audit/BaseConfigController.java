package com.mrmrscart.notificationreportlogservice.audit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mrmrscart.notificationreportlogservice.exception.UserIdNotFoundException;

@JsonIgnoreProperties
public class BaseConfigController {

	@Autowired
	private HttpServletRequest httpServletRequest;
	
	public String getUserId() {
		final String userIdTemp=httpServletRequest != null ?httpServletRequest.getHeader("userId"):"";
		if(userIdTemp!=null && !userIdTemp.equals("null") && !userIdTemp.equals("")) {
			return userIdTemp;
		}
		else{
			throw new UserIdNotFoundException("Logged-In userId not found!!");
		}
	}
}
