package com.mrmrscart.productcategoriesservice.pojo.product;

import java.time.LocalDateTime;
import java.util.List;

import com.mrmrscart.productcategoriesservice.entity.product.EUserType;

import lombok.Data;

@Data
public class ProductFlagPojo {
	
	private String flagId;
	private String flagTitle;
	private String productCategory;
	private String productType;
	private String imageType;
	private List<String> visibilityPlace;
	private String themeDetails;
	private String colorDetails;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	private String flagImageUrl;
	private List<String> variationId;
	private EUserType userType;
}
