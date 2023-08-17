package com.mrmrscart.userservice.pojo.admin;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mrmrscart.userservice.entity.admin.EStoreType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminMarketingToolsCampaignUpdatePojo {
	private Long adminMarketingToolsCampaignId;
	private String title;
	private BigDecimal price;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime startDateTime;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime endDateTime;
	
	private EStoreType storeType;
}
