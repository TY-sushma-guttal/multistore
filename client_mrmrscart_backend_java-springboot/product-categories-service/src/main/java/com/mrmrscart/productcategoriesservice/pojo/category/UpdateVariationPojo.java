package com.mrmrscart.productcategoriesservice.pojo.category;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateVariationPojo {

	private String subCategoryId;
	private String variationType;
	private String variationId;
	private String variationName;
	private List<UpdateVariationOptionPojo> updateVariationOptionPojos;
	private String createdBy;
	private String lastUpdatedBy;
	
	
		
}
