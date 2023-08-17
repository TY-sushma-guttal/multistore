package com.mrmrscart.productcategoriesservice.wrapper.category;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainCategoryFilterWrapper {

	private List<String> mainCategory;
	private List<String> commissionType;
}
