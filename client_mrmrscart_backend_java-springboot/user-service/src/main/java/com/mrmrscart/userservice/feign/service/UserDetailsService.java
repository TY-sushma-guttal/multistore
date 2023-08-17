package com.mrmrscart.userservice.feign.service;

import java.util.List;

import com.mrmrscart.userservice.feign.pojo.UserInfoWrapper;
import com.mrmrscart.userservice.feign.wrapper.UserDetailsResponseWrapper;

public interface UserDetailsService {

	public List<UserDetailsResponseWrapper> getUserDetails(UserInfoWrapper userInfoWrapper);

}
