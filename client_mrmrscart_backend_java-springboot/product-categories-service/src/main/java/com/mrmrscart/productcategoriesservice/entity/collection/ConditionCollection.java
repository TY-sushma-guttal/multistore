package com.mrmrscart.productcategoriesservice.entity.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "mmc_condition_collection")
public class ConditionCollection {
	@Id
	private String conditionCollectionId;
	private String conditionType;
	private boolean isDisable;
}
