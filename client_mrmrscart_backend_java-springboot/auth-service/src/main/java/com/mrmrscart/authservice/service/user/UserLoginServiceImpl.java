package com.mrmrscart.authservice.service.user;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import static com.mrmrscart.authservice.common.user.UserConstant.*;

import java.util.ArrayList;
import java.util.List;

import com.mrmrscart.authservice.entity.user.EUserRole;
import com.mrmrscart.authservice.entity.user.UserLogin;
import com.mrmrscart.authservice.entity.user.UserRole;
import com.mrmrscart.authservice.exception.user.PasswordNotMatchedException;
import com.mrmrscart.authservice.exception.user.UserException;
import com.mrmrscart.authservice.pojo.user.ChangePasswordPojo;
import com.mrmrscart.authservice.pojo.user.CustomerPojo;
import com.mrmrscart.authservice.pojo.user.ForgotPasswordPojo;
import com.mrmrscart.authservice.pojo.user.UserLoginPojo;
import com.mrmrscart.authservice.repository.user.UserLoginRepository;

@Service
public class UserLoginServiceImpl implements UserLoginService {

	@Autowired
	private UserLoginRepository userLoginRepository;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Override
	public UserLogin getUserByEmailId(String emailId) {
		return userLoginRepository.findByEmailId(emailId);

	}

	@Override
	public UserLogin addUserEmail(String emailId) {
		UserLogin userLogin = new UserLogin();
		userLogin.setEmailId(emailId);
		return userLoginRepository.save(userLogin);
	}

	@Override
	public UserLogin resetPassword(ForgotPasswordPojo forgotPasswordPojo) {
		UserLogin findByEmailId = userLoginRepository.findByEmailId(forgotPasswordPojo.getUserName());
		findByEmailId.setPassword(bcryptEncoder.encode(forgotPasswordPojo.getNewPassword()));
		return userLoginRepository.save(findByEmailId);
	}

	@Override
	public UserLogin changePassword(ChangePasswordPojo changePasswordPojo) {
		try {
			UserLogin findByEmailId = userLoginRepository.findByEmailId(changePasswordPojo.getEmailId());
			boolean matches = bcryptEncoder.matches(changePasswordPojo.getOldPassword(), findByEmailId.getPassword());
			if (matches) {
				findByEmailId.setPassword(bcryptEncoder.encode(changePasswordPojo.getNewPassword()));
				return userLoginRepository.save(findByEmailId);
			} else {
				throw new PasswordNotMatchedException(PASSWORD_NOT_MATCHED);
			}
		} catch (PasswordNotMatchedException e) {
			throw e;
		} catch (Exception e) {
			throw new UserException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public UserLogin addUser(UserLoginPojo data) {
		UserLogin userObj = new UserLogin();
		BeanUtils.copyProperties(data, userObj);
		userObj.setUserRoles(data.getUserRoles());
		return userLoginRepository.save(userObj);
	}

	@Override
	public UserLogin addCustomer(CustomerPojo customerPojo) {
		try {
			UserLogin findByEmailId = userLoginRepository.findByEmailId(customerPojo.getEmailId());
			if(findByEmailId==null) {
				UserLogin userLogin=new UserLogin();
				BeanUtils.copyProperties(customerPojo, userLogin);
				userLogin.setPassword(bcryptEncoder.encode(customerPojo.getPassword()));
				List<UserRole> userRoles=new ArrayList<>();
				UserRole userRole=new UserRole();
				userRole.setRole(EUserRole.CUSTOMER);
				userRoles.add(userRole);
				userLogin.setUserRoles(userRoles);
				return userLoginRepository.save(userLogin);
			}else {
				if(findByEmailId.getPassword()==null) {
					findByEmailId.setPassword(bcryptEncoder.encode(customerPojo.getPassword()));
				}
				List<UserRole> userRoles = findByEmailId.getUserRoles();
				UserRole userRole=new UserRole();
				userRole.setRole(EUserRole.CUSTOMER);
				userRoles.add(userRole);
				findByEmailId.setUserRoles(userRoles);
				return userLoginRepository.save(findByEmailId);
			}
		} catch (Exception e) {
			throw new UserException(SOMETHING_WENT_WRONG);
		}
	}
}
