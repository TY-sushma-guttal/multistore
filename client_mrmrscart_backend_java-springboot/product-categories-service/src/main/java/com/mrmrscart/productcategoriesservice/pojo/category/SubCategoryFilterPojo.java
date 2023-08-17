package com.mrmrscart.productcategoriesservice.pojo.category;

import java.time.LocalDateTime;
import java.util.List;

import com.mrmrscart.productcategoriesservice.entity.category.ECategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryFilterPojo {
	private List<ECategory> commissionModeList;
	private List<String> mainCategoryList;
	private List<String> setList;
	private List<String> subCategoryList;
	
	private LocalDateTime fromDate;
	private LocalDateTime toDate;
}
