package com.mrmrscart.helpandsupportservice.pojo;

import com.mrmrscart.helpandsupportservice.entity.EFilterType;
import com.mrmrscart.helpandsupportservice.entity.EUserType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HelpSupportFilterPojo {
	private String userId;
	private EUserType userFromType;
	private EUserType ticketType;
	private EFilterType filterType;
	private String keyword;
}
