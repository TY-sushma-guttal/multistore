package com.mrmrscart.helpandsupportservice.pojo;

import com.mrmrscart.helpandsupportservice.entity.ETicketStatus;
import com.mrmrscart.helpandsupportservice.entity.EUserType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketIssueTypeCountPojo {
	private EUserType userType;
	private ETicketStatus ticketStatus;
	private long orderRelatedIssueCount;
	private long paymentTransactionRelatedIssue;
	private long returnAndRefundCount; 
	private long logisticRelatedIssueCount; 
	private long cancellationAndRefundCount; 
	private long profileRelatedIssueCount;
	private long paymentSettlementIssueCount; 
	private long othersCount;
}
