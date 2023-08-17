package com.mrmrscart.authservice.service.user;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrmrscart.authservice.entity.user.UserLogin;
import com.mrmrscart.authservice.entity.user.UserRole;
import com.mrmrscart.authservice.exception.user.UserException;
import com.mrmrscart.authservice.exception.user.UserNameNotFoundException;
import com.mrmrscart.authservice.pojo.user.UserRolePojo;
import com.mrmrscart.authservice.repository.user.UserLoginRepository;
import static com.mrmrscart.authservice.common.user.UserConstant.SOMETHING_WENT_WRONG;
@Service
public class UserRoleServiceImpl implements UserRoleService{

	@Autowired
	private UserLoginRepository userLoginRepository;
	
	@Override
	public UserLogin addUserRole(UserRolePojo data) {
		try {
			UserLogin user = userLoginRepository.findByEmailId(data.getEmailId());
			if(user!=null) {
				UserRole userRole = new UserRole();
				userRole.setRole(data.getRole());
				user.getUserRoles().add(userRole);
				return userLoginRepository.save(user);
			}else {
				throw new UserNameNotFoundException("User is not present. ");
			}
		}catch(UserNameNotFoundException e) {
			throw e;
		}catch(Exception e) {
			throw new UserException(SOMETHING_WENT_WRONG);
		}
	}

}
