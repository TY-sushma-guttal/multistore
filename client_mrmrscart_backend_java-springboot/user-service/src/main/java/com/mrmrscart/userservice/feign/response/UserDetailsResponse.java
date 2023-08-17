package com.mrmrscart.userservice.feign.response;

import java.util.List;

import com.mrmrscart.userservice.feign.wrapper.UserDetailsResponseWrapper;

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
