package com.mrmrscart.productcategoriesservice.entity.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "mmc_capacity_collection")
public class CapacityCollection {
	@Id
	private String capacityCollectionId;
	private String capacityValue;
	private boolean isDisable;
}
