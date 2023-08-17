package com.mrmrscart.productcategoriesservice.entity.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "mmc_water_resistant_collection")
public class WaterResistant {
	@Id
	private String waterResistantId;
	private boolean isResistant;
	private boolean isDisable;
}

