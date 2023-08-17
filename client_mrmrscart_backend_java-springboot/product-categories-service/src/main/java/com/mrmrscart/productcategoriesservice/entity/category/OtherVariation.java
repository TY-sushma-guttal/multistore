package com.mrmrscart.productcategoriesservice.entity.category;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "mmc_other_variation")
public class OtherVariation {
	
	@Id
	private String otherVariationId;
	
	private String variationName;
	
	private String subCategoryId;
	
	private boolean isDisable;
	
	private boolean isDelete;
	
	private String variationType;
	
	@CreatedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime createdAt;

	@LastModifiedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime lastUpdatedAt;

	private String createdBy;
	
	private String lastUpdatedBy;
}
