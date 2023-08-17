package com.mrmrscart.productcategoriesservice.entity.product;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mrmrscart.productcategoriesservice.entity.category.EStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "mmc_product_tag")
public class ProductTag {
	
	@Id
	private String tagId;
	private String tagName;
	private EStatus status;
	private boolean isApproved;
	private boolean isDeleted;
	@CreatedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime createdAt;
	@LastModifiedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime lastUpdatedAt;
	private String createdBy;
	private String lastUpdatedBy;
	private EUserType createdByType;

}
