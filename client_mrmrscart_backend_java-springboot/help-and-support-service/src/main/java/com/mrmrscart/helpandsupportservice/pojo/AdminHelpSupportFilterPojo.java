package com.mrmrscart.helpandsupportservice.pojo;

import java.util.List;

import com.mrmrscart.helpandsupportservice.entity.EUserType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminHelpSupportFilterPojo {

	private List<Long> ticketId;
	private List<String> ticketStatus;
	private List<String> issueType;
	private EUserType userType;
}
