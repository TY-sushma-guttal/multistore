package com.mrmrscart.helpandsupportservice.pojo;

import com.mrmrscart.helpandsupportservice.entity.EUserType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketStatusCountPojo {
	private EUserType userType; 
	private long activeTicketCount;
	private long pendingTicketCount;
	private long closedTicketCount;
}
