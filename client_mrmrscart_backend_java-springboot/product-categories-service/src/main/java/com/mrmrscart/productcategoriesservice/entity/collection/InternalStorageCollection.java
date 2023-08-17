package com.mrmrscart.productcategoriesservice.entity.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "mmc_internal_storage_collection")
public class InternalStorageCollection {
	@Id
	private String internalStorageId;
	private String internalStorageCapacity;
	private boolean isDisable;
}
