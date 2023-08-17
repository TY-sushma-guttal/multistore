package com.mrmrscart.productcategoriesservice.entity.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "mmc_os_collection")
public class OsCollection {
	@Id
	private String osCollectionId;
	private String osCollectionName;
	private boolean isDisable;
}
