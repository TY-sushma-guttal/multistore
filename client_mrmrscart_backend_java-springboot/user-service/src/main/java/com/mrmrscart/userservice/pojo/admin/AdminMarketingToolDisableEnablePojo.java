package com.mrmrscart.userservice.pojo.admin;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminMarketingToolDisableEnablePojo {

	private List<Long> toolIdList;
	private boolean isDisabled;
}
