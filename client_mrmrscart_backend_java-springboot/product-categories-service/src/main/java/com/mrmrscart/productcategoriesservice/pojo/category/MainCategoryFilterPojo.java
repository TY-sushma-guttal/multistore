package com.mrmrscart.productcategoriesservice.pojo.category;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainCategoryFilterPojo {

	private LocalDateTime fromDate;
	private LocalDateTime toDate;
	private List<String> mainCategory;
	private List<String> commissionType;
}
