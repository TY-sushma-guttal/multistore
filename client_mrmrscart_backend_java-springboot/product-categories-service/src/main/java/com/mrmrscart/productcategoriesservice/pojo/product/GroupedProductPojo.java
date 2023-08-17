package com.mrmrscart.productcategoriesservice.pojo.product;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupedProductPojo {

	private String groupedProductId;

	private List<ChildProductPojo> childProducts;

	private boolean isDelete;
	
	private LocalDateTime createdAt;
	private LocalDateTime lastUpdatedAt;
	private String createdBy;
	private String lastUpdatedBy;
}
