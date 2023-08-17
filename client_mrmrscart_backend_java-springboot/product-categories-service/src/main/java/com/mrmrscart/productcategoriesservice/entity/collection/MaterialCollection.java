package com.mrmrscart.productcategoriesservice.entity.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "mmc_material_collection")
public class MaterialCollection {
	
	@Id
	private String materialCollectionId;
	private String materialName;
	private boolean isDisable;
}
