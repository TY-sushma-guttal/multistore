package com.mrmrscart.productcategoriesservice.pojo.product;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mrmrscart.productcategoriesservice.entity.category.EStatus;
import com.mrmrscart.productcategoriesservice.entity.product.EUserType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierFlagPojo {
	
	private String supplierFlagId;
	private String flagTitle;
	private String imageUrl;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime startDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime endDate;
	private List<String> variationList;
	private double discount;
	private EStatus	status;
	private String supplierStoreId;
	private String flagId;
	private String supplierId;
	private EUserType userType;
	private Long purchaseId;

}
