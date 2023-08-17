package com.mrmrscart.userservice.feign.pojo;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NegativeOrZero;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtherVariationPojo {
	private String otherVariationId;
	private String variationName;
	private boolean isDisable;
	private List<OtherVariationOptionPojo> optionList;
	private boolean isDelete;
	private String createdBy;
	private LocalDateTime createdAt;
	private LocalDateTime lastUpdatedAt;
	private String variationType;
	private String lastUpdatedBy;
}
