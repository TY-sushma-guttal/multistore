package com.mrmrscart.userservice.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mrmrscart.userservice.entity.admin.AdminRole;

public interface AdminRoleRepository extends JpaRepository<AdminRole, Long> {

	AdminRole findByAdminRoleTitleIgnoreCase(String string);

}
