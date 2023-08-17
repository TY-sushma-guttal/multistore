package com.mrmrscart.userservice.pojo.admin;

import java.util.List;

import com.mrmrscart.userservice.entity.admin.AdminMarketingTools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndividualPricingPojo {

	private String toolName;
	private List<AdminMarketingTools> adminMarketingTools;

}
