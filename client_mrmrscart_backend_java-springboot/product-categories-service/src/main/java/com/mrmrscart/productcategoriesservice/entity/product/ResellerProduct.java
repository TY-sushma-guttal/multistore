package com.mrmrscart.productcategoriesservice.entity.product;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mrmrscart.productcategoriesservice.pojo.product.ResellerProductMarginPojo;

import lombok.Data;

@Data
@Document(collection = "mmc_reseller_product")
public class ResellerProduct {
	@Id
	private String resellerProductId;
	private String resellerId;
	private List<ResellerProductMarginPojo> resellerProductsMargin;

}
