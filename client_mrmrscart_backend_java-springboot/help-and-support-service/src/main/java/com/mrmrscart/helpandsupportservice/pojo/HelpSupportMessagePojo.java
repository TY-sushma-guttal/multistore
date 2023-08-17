package com.mrmrscart.helpandsupportservice.pojo;

import java.util.List;

import com.mrmrscart.helpandsupportservice.entity.EUserType;
import com.mrmrscart.helpandsupportservice.entity.HelpSupportMessageMedia;

import lombok.Data;

@Data
public class HelpSupportMessagePojo {
	
	private Long ticketId;
	private String messageFromId;
	private EUserType messageFromType;
	private String message;
	private List<HelpSupportMessageMedia> imageUrlList;

}
