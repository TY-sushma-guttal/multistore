package com.mrmrscart.productcategoriesservice.wrapper.product;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mrmrscart.productcategoriesservice.entity.category.ECategory;

import lombok.Data;

@Document(collection = "mmc_product_categories")
@Data
public class ProductCategory {
	@Id
	private String masterProductId;
	private String categoryName;	
	private ECategory commissionType;

}
