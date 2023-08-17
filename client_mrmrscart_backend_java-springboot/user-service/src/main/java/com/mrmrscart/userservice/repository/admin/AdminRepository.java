package com.mrmrscart.userservice.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mrmrscart.userservice.entity.admin.AdminRegistration;

public interface AdminRepository extends JpaRepository<AdminRegistration, String>{

}
