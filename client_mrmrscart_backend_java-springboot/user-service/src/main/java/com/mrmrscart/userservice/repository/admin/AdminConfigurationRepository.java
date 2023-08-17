package com.mrmrscart.userservice.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mrmrscart.userservice.entity.admin.AdminConfiguration;

@Repository
public interface AdminConfigurationRepository extends JpaRepository<AdminConfiguration, Long>{

	AdminConfiguration findByAdminConfigurationName(String adminConfigurationName);
}
