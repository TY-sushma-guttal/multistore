package com.mrmrscart.productcategoriesservice.entity.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "mmc_pack_of_collection")
public class PackOfCollection {
	@Id
	private String packOfCollectionId;
	private String packOfName;
	private boolean isDisable;
}
