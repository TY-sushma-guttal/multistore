package com.mrmrscart.productcategoriesservice.audit;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import lombok.Data;

@Data
public abstract class Audit {

//	@CreatedDate
//	private LocalDateTime createdAt;
//	@LastModifiedDate
//	private LocalDateTime lastModifiedAt;

	@CreatedBy
	private String createdBy;

	@LastModifiedBy
	private String lastModifiedBy;

}
