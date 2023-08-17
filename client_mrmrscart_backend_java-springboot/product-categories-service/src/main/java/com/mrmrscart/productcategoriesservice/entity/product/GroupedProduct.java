package com.mrmrscart.productcategoriesservice.entity.product;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mrmrscart.productcategoriesservice.pojo.product.ChildProduct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "mmc_grouped_product")
public class GroupedProduct {
	@Id
	private String id;
	private String groupedProductId;
	
	private List<ChildProduct> childProducts;
	
	@CreatedDate
	private LocalDateTime createdAt;
	@LastModifiedDate
	private LocalDateTime lastUpdatedAt;
	private String createdBy;
	private String lastUpdatedBy;
	private boolean isDelete;
	
}
