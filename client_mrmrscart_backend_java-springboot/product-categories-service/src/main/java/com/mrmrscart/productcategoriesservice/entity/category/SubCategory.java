package com.mrmrscart.productcategoriesservice.entity.category;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mrmrscart.productcategoriesservice.pojo.category.SubCategoryStandardVariation;

import lombok.Data;

@Data
@Document(collection = "mmc_sub_category")
public class SubCategory {

	@Id
	private String subCategoryId;
	private String subCategoryName;
	private String setId;
	private String priceRangeId;
	private List<SubCategoryStandardVariation> standardVariationList;
	private boolean isDisable;
	private double supplierStorePercentage;
	private String mediaUrl;

	@CreatedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime createdAt;
	@LastModifiedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime lastUpdatedAt;

	private String createdBy;
	private String lastUpdatedBy;
}
