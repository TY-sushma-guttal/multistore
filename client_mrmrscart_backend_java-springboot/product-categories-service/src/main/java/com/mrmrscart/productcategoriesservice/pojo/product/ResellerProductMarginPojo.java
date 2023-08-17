package com.mrmrscart.productcategoriesservice.pojo.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ResellerProductMarginPojo {
	private String masterProductId;
	private String variationId;
	private BigDecimal productMargin;
	private LocalDateTime marginUpdatedAt;
}
