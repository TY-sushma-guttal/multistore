package com.mrmrscart.authservice.service.user;

import com.mrmrscart.authservice.entity.user.UserLogin;
import com.mrmrscart.authservice.pojo.user.ChangePasswordPojo;
import com.mrmrscart.authservice.pojo.user.CustomerPojo;
import com.mrmrscart.authservice.pojo.user.ForgotPasswordPojo;
import com.mrmrscart.authservice.pojo.user.UserLoginPojo;

public interface UserLoginService {

	public UserLogin getUserByEmailId(String emailId);

	public UserLogin addUserEmail(String emailId);

	public UserLogin resetPassword(ForgotPasswordPojo forgotPasswordPojo);

	public UserLogin changePassword(ChangePasswordPojo changePasswordPojo);

	public UserLogin addUser(UserLoginPojo data);

	public UserLogin addCustomer(CustomerPojo customerPojo);
}
