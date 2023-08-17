package com.mrmrscart.userservice.feign.pojo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainCategoryPojo {
	private String mainCategoryId;
	private String mainCategoryName;
	private ECategory commissionType;
	private String priceRangeId;
	private String categoryImageUrl;
	private String mainCategoryCode;
	private boolean isDisabled;
	private double supplierStorePercentage;
	private LocalDateTime createdAt;
	private LocalDateTime lastUpdatedAt;
	private String createdBy;
	private String lastUpdatedBy;

}
