package com.mrmrscart.helpandsupportservice.pojo;

import java.util.List;

import com.mrmrscart.helpandsupportservice.entity.EUserType;

import lombok.Data;

@Data
public class HelpSupportPojo {

	private String issueType;
	private String orderId;
	private String issueSubject;
	private EUserType userFromType;
	private String userFromId;
	private EUserType userToType;
	private String userToId;
	private List<String> mediaUrl;
	private List<HelpSupportMessagePojo> helpSupportMessagePojos;
	private String productVariationId;
}
