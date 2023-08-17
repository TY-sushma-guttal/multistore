package com.mrmrscart.helpandsupportservice.audit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mrmrscart.helpandsupportservice.exception.HelpSupportException;

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
			throw new HelpSupportException("Logged-In userId not found!!");
		}
	}
}
