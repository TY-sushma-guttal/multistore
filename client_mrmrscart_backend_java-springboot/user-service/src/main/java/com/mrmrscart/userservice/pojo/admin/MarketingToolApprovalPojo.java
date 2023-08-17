package com.mrmrscart.userservice.pojo.admin;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mrmrscart.userservice.entity.admin.EMarketingTools;
import com.mrmrscart.userservice.entity.reseller.MarketingToolQuestionAnswer;

import lombok.Data;

@Data
public class MarketingToolApprovalPojo {
	
	private Long marketingToolId;
	private EMarketingTools toolType;
	private String description;
	private String campaignTitle;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime startDateTime;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime endDateTime;
	private String userType;
	private String userTypeId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime createdDate;
	private List<MarketingToolQuestionAnswer> marketingToolQuestionAnswers;
}
