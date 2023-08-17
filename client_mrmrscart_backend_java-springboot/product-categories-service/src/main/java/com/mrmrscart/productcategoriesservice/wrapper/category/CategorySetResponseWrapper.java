package com.mrmrscart.productcategoriesservice.wrapper.category;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorySetResponseWrapper {
	private String categorySetId;
	private String setName;
	private String mainCategoryName;
	private LocalDateTime createdAt;
	private LocalDateTime lastUpdatedAt;
	private String categorySetImageUrl;
	private boolean isDisabled;
	private String createdBy;

}
