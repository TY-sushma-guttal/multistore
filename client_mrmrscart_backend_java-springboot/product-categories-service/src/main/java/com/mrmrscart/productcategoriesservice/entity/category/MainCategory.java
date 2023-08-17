package com.mrmrscart.productcategoriesservice.entity.category;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Document(collection = "mmc_main_category")
@Data
public class MainCategory {
	@Id
	private String mainCategoryId;
	
	private String mainCategoryName;
	
	private ECategory commissionType;
	
	private String priceRangeId;
	
	private String categoryImageUrl;
	
	private String mainCategoryCode;
	
	private boolean isDisabled;
	
	private double supplierStorePercentage;
	
	@CreatedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime createdAt;

	@LastModifiedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime lastUpdatedAt;

	private String createdBy;
	
	private String lastUpdatedBy;
}
