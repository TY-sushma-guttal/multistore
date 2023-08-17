package com.mrmrscart.productcategoriesservice.pojo.product;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPolicyPojo {
	
	private String policyTabLabel;
	private String shippingPolicy;
	private List<String> shippingPolicyMediaUrls;
	private String refundPolicy;
	private List<String> refundPolicyMediaUrls;
	private String cancellationPolicy;
	private List<String> cancellationPolicyMediaUrls;
	
	private boolean warrantyAvailable;
	private int warrantyPeriod;
}
