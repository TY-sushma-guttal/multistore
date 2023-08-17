package com.mrmrscart.authservice.service.user;

import static com.mrmrscart.authservice.common.user.UserConstant.JWT_INVALID;
import static com.mrmrscart.authservice.common.user.UserConstant.JWT_MESSAGE;
import static com.mrmrscart.authservice.common.user.UserConstant.LOGIN_SUCCESS;
import static com.mrmrscart.authservice.common.user.UserConstant.SOMETHING_WENT_WRONG;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mrmrscart.authservice.entity.user.EUserRole;
import com.mrmrscart.authservice.entity.user.UserLogin;
import com.mrmrscart.authservice.exception.user.InvalidCredentailException;
import com.mrmrscart.authservice.exception.user.UnauthorizedException;
import com.mrmrscart.authservice.exception.user.UserNameNotFoundException;
import com.mrmrscart.authservice.feign.pojo.AuthenticationOtpPojo;
import com.mrmrscart.authservice.feign.pojo.AuthenticationPojo;
import com.mrmrscart.authservice.feign.response.EmailResponse;
import com.mrmrscart.authservice.feign.response.StaffManagementInfoResponse;
import com.mrmrscart.authservice.feign.user.UserServiceClient;
import com.mrmrscart.authservice.pojo.user.AdminRegistrationPojo;
import com.mrmrscart.authservice.pojo.user.UserLoginRequestPojo;
import com.mrmrscart.authservice.repository.user.UserLoginRepository;
import com.mrmrscart.authservice.response.user.AdminRegistrationResponse;
import com.mrmrscart.authservice.response.user.AuthenticateOtpResponse;
import com.mrmrscart.authservice.response.user.AuthenticateResponse;

import feign.FeignException;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	private UserServiceClient userServiceClient;

	@Autowired
	private UserLoginRepository userLoginRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Value("${jwt.secret}")
	private String secret;

	@Override
	public AuthenticateResponse createAuthenticationToken(UserLoginRequestPojo userLoginRequestPojo) {
		try {
			AuthenticateResponse authenticateResponse = new AuthenticateResponse();
			AuthenticationPojo authenticationPojo = new AuthenticationPojo(userLoginRequestPojo.getUserName(),
					userLoginRequestPojo.getUserType());
			ResponseEntity<EmailResponse> verifyUser = userServiceClient.verifyUser(authenticationPojo);
			EmailResponse body = verifyUser.getBody();

			if (body != null) {
				String emailId = body.getEmailId();

				authenticate(emailId, userLoginRequestPojo.getPassword());
				UserLogin findByEmailId = userLoginRepository.findByEmailId(emailId);
				String token = getToken(findByEmailId, body.getUserId());
				long count = findByEmailId.getUserRoles().stream().filter(e -> e.getRole() == EUserRole.STAFF).count();
				if (count > 0) {
					ResponseEntity<StaffManagementInfoResponse> staffManagementInfo = userServiceClient
							.getStaffManagementInfo(emailId);
					StaffManagementInfoResponse body2 = staffManagementInfo.getBody();
					if (body2 != null) {
						authenticateResponse.setStaffDetails(body2.getStaffManagementInfo());
					} else {
						throw new UserNameNotFoundException(SOMETHING_WENT_WRONG);
					}
				} else {
					long admin = findByEmailId.getUserRoles().stream().filter(e -> e.getRole() == EUserRole.ADMIN)
							.count();
					if (admin > 0) {
						AdminRegistrationPojo adminDetails = getAdminDetails(emailId);
						authenticateResponse.setAdminRegistrationPojo(adminDetails);
					}
				}
				authenticateResponse.setToken(token);
				authenticateResponse.setError(false);
				authenticateResponse.setMessage(LOGIN_SUCCESS);
				return authenticateResponse;
			} else {
				throw new UserNameNotFoundException(SOMETHING_WENT_WRONG);
			}
		} catch (FeignException | UnauthorizedException | InvalidCredentailException e) {
			throw e;
		} catch (Exception e) {
			throw new UserNameNotFoundException(SOMETHING_WENT_WRONG);
		}
	}

	private void authenticate(String userName, String password)
			throws DisabledException, BadCredentialsException, UnauthorizedException, InvalidCredentailException {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
		} catch (DisabledException e) {
			throw new UnauthorizedException(JWT_MESSAGE);
		} catch (BadCredentialsException e) {
			throw new InvalidCredentailException(JWT_INVALID);
		}
	}

	private String getToken(UserLogin findByEmailId, String userId) {
		List<String> roles = new ArrayList<>();
		findByEmailId.getUserRoles().forEach(e -> roles.add(e.getRole().name()));
		Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
		return JWT.create().withSubject(userId + "," + findByEmailId.getEmailId())
				.withExpiresAt(new Date(System.currentTimeMillis() + 90 * 60 * 1000)).withClaim("roles", roles)
				.sign(algorithm);

	}

	@Override
	public AuthenticateOtpResponse createOtpAuthenticationToken(AuthenticationOtpPojo authenticationPojo) {
		UserLogin findByEmailId = userLoginRepository.findByEmailId(authenticationPojo.getUserName());
		long count = findByEmailId.getUserRoles().stream().filter(e -> e.getRole() == EUserRole.STAFF).count();
		AuthenticateOtpResponse authenticateOtpResponse = new AuthenticateOtpResponse();
		authenticateOtpResponse.setError(false);
		authenticateOtpResponse.setMessage(LOGIN_SUCCESS);
		if (count > 0) {
			authenticateOtpResponse.setStaff(true);
		}
		authenticateOtpResponse.setToken(getToken(findByEmailId, authenticationPojo.getUserId()));
		return authenticateOtpResponse;

	}

	private AdminRegistrationPojo getAdminDetails(String emailId) {
		try {
			ResponseEntity<AdminRegistrationResponse> adminInfo = userServiceClient.getAdminInfo(emailId);
			AdminRegistrationResponse registrationResponse = adminInfo.getBody();
			if (registrationResponse != null) {
				return registrationResponse.getData();
			} else {
				throw new UserNameNotFoundException(SOMETHING_WENT_WRONG);
			}
		} catch (UserNameNotFoundException | FeignException e) {
			throw e;
		} catch (Exception e) {
			throw new UserNameNotFoundException(SOMETHING_WENT_WRONG);
		}
	}

}
