package com.mrmrscart.userservice.repository.reseller;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mrmrscart.userservice.entity.admin.EMarketingToolStatus;
import com.mrmrscart.userservice.entity.admin.EMarketingTools;
import com.mrmrscart.userservice.entity.reseller.UserMarketingTool;

@Repository
public interface UserMarketingToolRepository extends JpaRepository<UserMarketingTool, Long> {

	UserMarketingTool findByMarketingToolIdAndToolStatus(Long marketingToolId, EMarketingToolStatus status);

	public List<UserMarketingTool> findByToolStatusAndIsDelete(EMarketingToolStatus status, boolean b);

	public UserMarketingTool findByMarketingToolIdAndToolStatusAndIsDelete(Long marketingToolId,
			EMarketingToolStatus toolStatus, boolean isDelete);

	public List<UserMarketingTool> findByUserTypeIdAndToolTypeAndIsDelete(String userTypeId, EMarketingTools toolType,
			boolean isDelete);

	List<UserMarketingTool> findByToolTypeAndToolStatusAndIsDelete(EMarketingTools marketingToolType,
			EMarketingToolStatus pending, boolean b);
}
