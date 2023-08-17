package com.mrmrscart.productcategoriesservice.pojo.collection;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColorCollectionPojo {
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
