package com.mrmrscart.helpandsupportservice.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.mrmrscart.helpandsupportservice.feign.pojo.UserInfoWrapper;
import com.mrmrscart.helpandsupportservice.feign.response.UserDetailsResponse;

@FeignClient(name = "user-service")
public interface UserServiceClient {

	@PostMapping("/api/v1/users/user-info")
	public ResponseEntity<UserDetailsResponse> getUserDetails(@RequestBody UserInfoWrapper wrapper);
}
