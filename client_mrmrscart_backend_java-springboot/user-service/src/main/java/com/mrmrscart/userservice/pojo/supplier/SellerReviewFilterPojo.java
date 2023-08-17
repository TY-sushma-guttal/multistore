package com.mrmrscart.userservice.pojo.supplier;

import com.mrmrscart.userservice.entity.supplier.ESellerReviewFilter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerReviewFilterPojo {

	private ESellerReviewFilter filterType;
	private String keyword;
}
