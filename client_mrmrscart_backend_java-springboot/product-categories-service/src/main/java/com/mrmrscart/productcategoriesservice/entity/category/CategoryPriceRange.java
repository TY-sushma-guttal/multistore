package com.mrmrscart.productcategoriesservice.entity.category;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mrmrscart.productcategoriesservice.pojo.category.PriceRange;

import lombok.Data;

@Data
@Document(collection = "mmc_price_range_collection")
public class CategoryPriceRange {
	@Id
	private String priceRangeId;
	
	private List<PriceRange> priceRange;
	
	@CreatedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime createdAt;
	
	@LastModifiedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime lastUpdatedAt;
	
	private String createdBy;
	
	private String lastUpdatedBy;
}
