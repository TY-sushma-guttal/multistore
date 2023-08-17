package com.mrmrscart.productcategoriesservice.entity.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "mmc_occassion_collection")
public class OccassionCollection {

	@Id
	private String occassionCollectionId;
	private String occassionName;
	private boolean isDisable;
}
