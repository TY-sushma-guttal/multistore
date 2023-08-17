package com.mrmrscart.userservice.feign.pojo;

import java.time.LocalDateTime;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtherVariationOptionPojo {
	private String otherVariationOptionId;
	private String optionName;
	private boolean isDisable;
	private boolean isDelete;
	private String createdBy;
	private LocalDateTime createdAt;
	private LocalDateTime lastUpdatedAt;
	private String lastUpdatedBy;
}
