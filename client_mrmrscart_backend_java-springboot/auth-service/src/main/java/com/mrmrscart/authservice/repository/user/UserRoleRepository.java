package com.mrmrscart.authservice.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mrmrscart.authservice.entity.user.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer>{
	
}
