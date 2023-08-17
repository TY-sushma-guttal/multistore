package com.mrmrscart.productcategoriesservice.entity.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "mmc_washcare_collection")
public class WashcareCollection {
	@Id
	private String washcareCollectionId;
	private String washcareName;
	private boolean isDisable;
}
