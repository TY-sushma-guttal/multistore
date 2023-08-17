package com.mrmrscart.productcategoriesservice.pojo.product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mrmrscart.productcategoriesservice.entity.category.ECategory;
import com.mrmrscart.productcategoriesservice.entity.category.EStatus;

import lombok.Data;

@Data
public class ProductFilterPojo {

	private List<String> categoryIds=new ArrayList<>();
	private List<String> subCategoryIds=new ArrayList<>();
	private List<String> brandNames=new ArrayList<>();
	private List<String> productVariationIds=new ArrayList<>();
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime dateFrom;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime dateTo;
	private ECategory commissionType;
	private EStatus status;
}
