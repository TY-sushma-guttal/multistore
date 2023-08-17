package com.mrmrscart.productcategoriesservice.entity.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "mmc_pattern_collection")
public class PatternCollection {
	@Id
	private String patternCollectionId;
	private String patternName;
	private boolean isDisable;
}
