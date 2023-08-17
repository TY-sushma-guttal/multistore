package com.mrmrscart.userservice.pojo.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionCountPojo {

	private String adminMarketingToolName;
	private int totalCount;
	private Long activeCount;
	private Long inActiveCount;
}
