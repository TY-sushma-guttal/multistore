package com.mrmrscart.productcategoriesservice.entity.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "mmc_age_collection")
public class AgeCollection {
	@Id
	private String ageCollectionId;
	private String ageValue;
	private boolean isDisable;
}
