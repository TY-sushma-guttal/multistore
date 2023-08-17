package com.mrmrscart.productcategoriesservice.entity.category;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "mmc_set")
@AllArgsConstructor
@NoArgsConstructor
public class CategorySet {
	@Id
	private String categorySetId;
	private String setName;
	private String mainCategoryId;
	@CreatedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime createdAt;
	@LastModifiedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime lastUpdatedAt;
	@CreatedBy
	private String createdBy;
	@LastModifiedBy
	private String lastUpdatedBy;
	private boolean isDisabled;
	private String categorySetImageUrl;
}
