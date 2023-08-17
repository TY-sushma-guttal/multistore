package com.mrmrscart.userservice.pojo.admin;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminMarketingToolsPricePojo {

	private Long toolId;
	private BigDecimal price;
}
