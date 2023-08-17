package com.mrmrscart.helpandsupportservice.service;

import java.util.List;

import com.mrmrscart.helpandsupportservice.entity.EUserType;
import com.mrmrscart.helpandsupportservice.entity.HelpSupport;
import com.mrmrscart.helpandsupportservice.pojo.HelpSupportFilterPojo;
import com.mrmrscart.helpandsupportservice.pojo.HelpSupportMessagePojo;
import com.mrmrscart.helpandsupportservice.pojo.HelpSupportMessageViewPojo;
import com.mrmrscart.helpandsupportservice.pojo.HelpSupportPojo;
import com.mrmrscart.helpandsupportservice.pojo.TicketIssueTypeCountPojo;
import com.mrmrscart.helpandsupportservice.pojo.TicketStatusCountPojo;

public interface HelpSupportService {

	public HelpSupport createTicket(HelpSupportPojo helpSupportPojo);

	public List<HelpSupport> getAllSupplierTickets(int pageNumber, int pageSize, HelpSupportFilterPojo pojo);

	public boolean viewTicket(HelpSupportMessageViewPojo messageViewPojo);

	public boolean sendReply(HelpSupportMessagePojo helpSupportMessagePojo);

	public List<TicketStatusCountPojo> getDashboardData();

	public List<TicketIssueTypeCountPojo> getTicketCountByIssueTypeAndStatus(EUserType userFromType);
}
