package com.mrmrscart.productcategoriesservice.entity.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "mmc_power_type_collection")
public class PowerTypeCollection {
	@Id
	private String powerTypeCollectionId;
	private String powerTypeName;
	private boolean isDisable;
}
