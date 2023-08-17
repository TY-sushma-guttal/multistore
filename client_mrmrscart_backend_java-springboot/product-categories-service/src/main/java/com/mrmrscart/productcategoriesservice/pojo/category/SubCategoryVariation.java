package com.mrmrscart.productcategoriesservice.pojo.category;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryVariation {
	private String subCategoryId;
	private List<SubCategoryStandardVariation> standardVariationList;
	private List<OtherVariationPojo> otherVariationList; 
}
