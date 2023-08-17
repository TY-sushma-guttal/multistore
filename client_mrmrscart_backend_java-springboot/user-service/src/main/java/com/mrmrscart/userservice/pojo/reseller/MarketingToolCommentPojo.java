package com.mrmrscart.userservice.pojo.reseller;

import com.mrmrscart.userservice.entity.reseller.EMarketingToolCommentsType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketingToolCommentPojo {

	private EMarketingToolCommentsType type;
	private Long typeId;
	private String comments;
	private String commentsAttachment;
}
