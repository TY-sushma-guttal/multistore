package com.mrmrscart.userservice.service.admin;

import static com.mrmrscart.userservice.common.admin.AdminSupplierConstant.*;
import static com.mrmrscart.userservice.common.supplier.SupplierConstant.DEBUG_MESSAGE;
import static com.mrmrscart.userservice.common.supplier.SupplierConstant.ERROR_MESSAGE;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrmrscart.userservice.entity.admin.AdminConfiguration;
import com.mrmrscart.userservice.exception.admin.AdminException;
import com.mrmrscart.userservice.pojo.admin.AdminConfigurationPojo;
import com.mrmrscart.userservice.repository.admin.AdminConfigurationRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminConfigurationServiceImpl implements AdminConfigurationService {

	@Autowired
	private AdminConfigurationRepository adminConfigurationRepository;

	@Override
	public AdminConfiguration addAdminConfiguration(AdminConfigurationPojo adminConfigurationPojo) {
		try {
			log.error(DEBUG_MESSAGE);
			AdminConfiguration findByAdminConfigurationName = adminConfigurationRepository
					.findByAdminConfigurationName(adminConfigurationPojo.getAdminConfigurationName());
			if (findByAdminConfigurationName == null) {
				AdminConfiguration adminConfiguration = new AdminConfiguration();
				BeanUtils.copyProperties(adminConfigurationPojo, adminConfiguration);
				return adminConfigurationRepository.save(adminConfiguration);
			} else {
				log.error(DEBUG_MESSAGE);
				throw new AdminException(CONFIGURATION_EXIST);
			}
		} catch (AdminException e) {
			log.error(ERROR_MESSAGE);
			throw e;
		} catch (Exception e) {
			log.error(ERROR_MESSAGE);
			e.printStackTrace();
			throw new AdminException(SOMETHING_WENT_WRONG);
		}

	}

}
