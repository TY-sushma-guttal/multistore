package com.mrmrscart.helpandsupportservice.feign.response;

import java.util.List;

import com.mrmrscart.helpandsupportservice.feign.pojo.UserDetailsResponseWrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsResponse {
	private boolean error;
	private String message;
	private List<UserDetailsResponseWrapper> data;
}
