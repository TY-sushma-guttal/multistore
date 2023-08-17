package com.mrmrscart.productcategoriesservice.wrapper.category;


import java.util.List;

import com.mrmrscart.productcategoriesservice.pojo.category.OtherVariationPojo;
import com.mrmrscart.productcategoriesservice.pojo.category.SubCategoryStandardVariation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryVariationWrapper {
	private String subCategoryId;
	private List<OtherVariationPojo> otherVariationList;
	private List<SubCategoryStandardVariation> standardVariationList;
}
