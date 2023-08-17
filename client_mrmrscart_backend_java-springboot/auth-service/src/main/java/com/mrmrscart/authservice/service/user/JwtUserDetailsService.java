package com.mrmrscart.authservice.service.user;

import static com.mrmrscart.authservice.common.user.UserConstant.SET_PASSWORD;
import static com.mrmrscart.authservice.common.user.UserConstant.USER_NOT_FOUND;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mrmrscart.authservice.entity.user.UserLogin;
import com.mrmrscart.authservice.repository.user.UserLoginRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService{
	
	@Autowired
	private UserLoginRepository userLoginRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       UserLogin findByEmailId = userLoginRepository.findByEmailId(username);
       if(findByEmailId==null) {
    	   throw new UsernameNotFoundException(USER_NOT_FOUND+username);
       }else if(findByEmailId.getPassword()==null) {
    	   throw new UsernameNotFoundException(SET_PASSWORD);
       }
       List<SimpleGrantedAuthority> authorities=new ArrayList<>();
       findByEmailId.getUserRoles().forEach(x -> authorities.add(new SimpleGrantedAuthority("ROLE_"+x.getRole())));
		return new org.springframework.security.core.userdetails.User(findByEmailId.getEmailId(), findByEmailId.getPassword(), authorities);
		
	}

}
