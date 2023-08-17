package com.mrmrscart.productcategoriesservice.entity.collection;

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
@Document(collection = "mmc_color_collection")
@AllArgsConstructor
@NoArgsConstructor
public class ColorCollection {
	@Id
	private String colorCollectionId;

	private String colorName;
	
	private String colorCode;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	@CreatedDate
	private LocalDateTime createdAt;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	@LastModifiedDate
	private LocalDateTime lastUpdatedAt;
	
	private String createdBy;
	
	private String lastUpdatedBy;
}
