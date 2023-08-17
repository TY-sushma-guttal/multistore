package com.mrmrscart.userservice.feign.pojo;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategorySetPojo {
	private String categorySetId;
	private String setName;
	private String mainCategoryId;
	private LocalDateTime createdAt;
	private LocalDateTime lastUpdatedAt;
	private String createdBy;
	private String lastUpdatedBy;
	private boolean isDisabled;
	private String categorySetImageUrl;
}
