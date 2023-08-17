package com.mrmrscart.authservice.service.user;

import com.mrmrscart.authservice.feign.pojo.AuthenticationOtpPojo;
import com.mrmrscart.authservice.pojo.user.UserLoginRequestPojo;
import com.mrmrscart.authservice.response.user.AuthenticateOtpResponse;
import com.mrmrscart.authservice.response.user.AuthenticateResponse;

public interface AuthenticationService {

	AuthenticateResponse createAuthenticationToken(UserLoginRequestPojo userLoginRequestPojo);

	AuthenticateOtpResponse createOtpAuthenticationToken(AuthenticationOtpPojo authenticationPojo);

}
