package com.mrmrscart.productcategoriesservice.wrapper.category;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.mrmrscart.productcategoriesservice.entity.category.ECategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryResponseWrapper {
	private String subCategoryId;
	private String subCategoryName;
	private LocalDateTime createdAt;
	
	private String mainCategoryName;
	private ECategory commissionType;
	private double commissionPercentage;
	private double mmcartProfitePercentage;
	private double resellerProfitPercentage;
}
