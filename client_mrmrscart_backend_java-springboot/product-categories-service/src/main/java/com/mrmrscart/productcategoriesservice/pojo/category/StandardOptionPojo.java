package com.mrmrscart.productcategoriesservice.pojo.category;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandardOptionPojo {

	private String standardOptionId;
	private List<String> optionName;
	private String standardVariationId;
	
	private LocalDateTime createdAt;
	private LocalDateTime lastUpdatedAt;
	
	private String createdBy;
	private String lastUpdatedBy;
}
