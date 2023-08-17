package com.mrmrscart.helpandsupportservice.pojo;

import com.mrmrscart.helpandsupportservice.entity.EUserType;

import lombok.Data;

@Data
public class HelpSupportMessageViewPojo {
	
	private Long ticketId;
	private EUserType viewedByType;
	private String viewedById;

}
