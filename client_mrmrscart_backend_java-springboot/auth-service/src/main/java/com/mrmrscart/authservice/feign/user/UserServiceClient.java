package com.mrmrscart.authservice.feign.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.mrmrscart.authservice.feign.pojo.AuthenticationPojo;
import com.mrmrscart.authservice.feign.response.EmailResponse;
import com.mrmrscart.authservice.feign.response.StaffManagementInfoResponse;
import com.mrmrscart.authservice.response.user.AdminRegistrationResponse;

@FeignClient(name = "user-service")
public interface UserServiceClient {

	@PostMapping("/api/v1/users/verify-user")
	public ResponseEntity<EmailResponse> verifyUser(@RequestBody AuthenticationPojo authenticationPojo);

	@GetMapping("/api/v1/users/staff/{emailId}")
	public ResponseEntity<StaffManagementInfoResponse> getStaffManagementInfo(@PathVariable String emailId);

	@GetMapping("/api/v1/users/admin/verify-admin")
	public ResponseEntity<AdminRegistrationResponse> getAdminInfo(@RequestParam("emailId") String emailId);
}
