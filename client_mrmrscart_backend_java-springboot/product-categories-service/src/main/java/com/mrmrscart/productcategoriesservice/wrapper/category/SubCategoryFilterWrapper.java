package com.mrmrscart.productcategoriesservice.wrapper.category;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryFilterWrapper {
	
	private List<SubCategoryWrapper> subCategoryList;
	private List<MainCategoryWrapper> mainCategoryList;
	private List<String> commissionModeList;
}
