package com.mrmrscart.productcategoriesservice.entity.product;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mrmrscart.productcategoriesservice.audit.Audit;
import com.mrmrscart.productcategoriesservice.entity.category.EStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@Document(collection = "mmc_product_flag")
@AllArgsConstructor
@NoArgsConstructor
public class ProductFlag extends Audit {
	@Id
	private String flagId;
	private String flagTitle;
	private String productCategory;
	private String productType;
	private String imageType;
	private List<String> visibilityPlace;
	private String themeDetails;
	private String colorDetails;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime startDateTime;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime endDateTime;
	private EStatus flagStatus;
	private String flagImageUrl;
	private List<String> variationId;
	@CreatedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime createdAt;
	@LastModifiedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime lastUpdatedAt;
	private boolean isEnabled;
	private boolean isDeleted;
	private EUserType userType;
}
