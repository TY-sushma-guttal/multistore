package com.mrmrscart.productcategoriesservice.pojo.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandardVariationPojo {

	private String standardVariationId;
	private String variationName;
	private String createdBy;
	private String lastUpdatedBy;
}	
