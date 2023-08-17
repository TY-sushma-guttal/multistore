package com.mrmrscart.helpandsupportservice.service;

import java.util.List;

import com.mrmrscart.helpandsupportservice.entity.EUserType;
import com.mrmrscart.helpandsupportservice.entity.HelpSupport;
import com.mrmrscart.helpandsupportservice.pojo.AdminHelpSupportFilterPojo;
import com.mrmrscart.helpandsupportservice.pojo.HelpSupportPojo;
import com.mrmrscart.helpandsupportservice.pojo.SupplierQueryPojo;
import com.mrmrscart.helpandsupportservice.wrapper.AdminHelpSupportFilterWrapper;
import com.mrmrscart.helpandsupportservice.wrapper.HelpSupportQueryWrapper;
import com.mrmrscart.helpandsupportservice.wrapper.HelpSupportWrapper;

public interface AdminHelpSupportService {

	public boolean deleteTicket(Long ticketId);

	public boolean closeTicket(Long ticketId);

	public List<HelpSupportWrapper> getTicketsByUserType(AdminHelpSupportFilterPojo filterPojo, int pageNumber,
			int pageSize);

	public List<AdminHelpSupportFilterWrapper> getFilterInfo(EUserType userType);
	
	public HelpSupport createProductTickets(HelpSupportPojo helpSupportPojo);

	public List<HelpSupportQueryWrapper> getSupplierQueries(SupplierQueryPojo pojo, int pageNumber, int pageSize);
}
