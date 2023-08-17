package com.mrmrscart.productcategoriesservice.entity.category;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@Document(collection = "mmc_supplier_variation_option")
public class SupplierVariationOption {
	@Id
	private String variationOptionId;
	private String mainCategoryId;
	private String setId;
	private String subCategoryId;
	private String supplierId;
	private String variationId;
	private String variationName;
	private List<String> optionName;
	@CreatedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime createdAt;
	@LastModifiedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime lastUpdatedAt;
	private String lastUpdatedBy;
	private EStatus approvalStatus;

}
