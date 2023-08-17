package com.mrmrscart.notificationreportlogservice.pojo;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.mrmrscart.notificationreportlogservice.entity.EMarketingToolPricingType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceChangeHistoryPojo {
	@NotEmpty
	private List<@NotNull Long> toolId;
	private EMarketingToolPricingType type;
}
