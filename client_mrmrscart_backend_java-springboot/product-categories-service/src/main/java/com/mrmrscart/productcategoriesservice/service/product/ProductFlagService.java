package com.mrmrscart.productcategoriesservice.service.product;

import java.time.LocalDateTime;
import java.util.List;

import com.mrmrscart.productcategoriesservice.entity.product.ProductFlag;
import com.mrmrscart.productcategoriesservice.pojo.product.ProductFlagPojo;
import com.mrmrscart.productcategoriesservice.pojo.product.ProductFlagingPojo;

public interface ProductFlagService {
	
	public ProductFlag addProductFlag(ProductFlagPojo productFlagPojo);
	
	public boolean enableProductFlag(String flagId,boolean isEnabled);
	
	public ProductFlag updateProductFlag(ProductFlagPojo productFlagPojo);
	
	public boolean deleteProductFlag(String flagId);
	
	public List<ProductFlag> getAllFlags();
	
	public ProductFlag getById(String flagId);
	
	public List<ProductFlag> getAllFilteredFlags(LocalDateTime dateFrom,LocalDateTime dateTo);
	
	public String updateProductFlag(ProductFlagingPojo productFlagingPojo);
	
}
