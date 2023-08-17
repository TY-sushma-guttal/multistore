package com.mrmrscart.productcategoriesservice.pojo.category;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorySetFilter {
	private List<String> mainCategory;
	private List<String> categorySet;
	private LocalDateTime fromDate;
	private LocalDateTime toDate;
}
