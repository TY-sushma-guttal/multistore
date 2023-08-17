package com.mrmrscart.productcategoriesservice.entity.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "mmc_ideal_for_collection")
public class IdealForCollection {
	@Id
	private String idealForCollectionId;
	private String idealForType;
	private boolean isDisable;
}
