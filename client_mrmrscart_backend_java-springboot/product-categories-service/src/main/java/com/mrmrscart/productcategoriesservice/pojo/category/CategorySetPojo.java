package com.mrmrscart.productcategoriesservice.pojo.category;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategorySetPojo {

	private String categorySetId;
	private String setName;
	private String mainCategoryId;
	private LocalDateTime createdAt;
	private LocalDateTime lastUpdatedAt;
	private String createdBy;
	private String lastUpdatedBy;
	private boolean isDisabled;
	private String categorySetImageUrl;
}
