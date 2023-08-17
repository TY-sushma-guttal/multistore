package com.mrmrscart.productcategoriesservice.entity.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "mmc_sleeves_collection")
public class SleevesCollection {
	@Id
	private String sleevesCollectionId;
	private String sleevesName;
	private boolean isDisable;
}
