package com.mrmrscart.productcategoriesservice.pojo.category;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtherVariationOptionPojo {
	private String otherVariationOptionId;
	private String optionName;
	private boolean isDisable;
	private boolean isDelete;
	private String createdBy;
	private LocalDateTime createdAt;
	private LocalDateTime lastUpdatedAt;

	private String lastUpdatedBy;
}
