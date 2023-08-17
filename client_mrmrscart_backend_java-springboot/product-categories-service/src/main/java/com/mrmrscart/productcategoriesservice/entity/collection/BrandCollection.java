package com.mrmrscart.productcategoriesservice.entity.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "mmc_brand_collection")
public class BrandCollection {
	@Id
	private String brandCollectionId;
	private String brandType;
	private String brandName;
	private boolean isDisable;
}
