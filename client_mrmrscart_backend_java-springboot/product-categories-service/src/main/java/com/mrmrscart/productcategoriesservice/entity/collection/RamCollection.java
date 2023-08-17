package com.mrmrscart.productcategoriesservice.entity.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "mmc_ram_collection")
public class RamCollection {
	@Id
	private String ramCollectionId;
	private String ramCapacity;
	private boolean isDisable;
}
