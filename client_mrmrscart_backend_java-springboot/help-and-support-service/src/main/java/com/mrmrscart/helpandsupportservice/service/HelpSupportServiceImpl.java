package com.mrmrscart.helpandsupportservice.service;

import static com.mrmrscart.helpandsupportservice.common.HelpSupportConstant.SOMETHING_WENT_WRONG;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.mrmrscart.helpandsupportservice.common.HelpSupportConstant.*;
import com.mrmrscart.helpandsupportservice.entity.EFilterType;
import com.mrmrscart.helpandsupportservice.entity.EIssueType;
import com.mrmrscart.helpandsupportservice.entity.ETicketStatus;
import com.mrmrscart.helpandsupportservice.entity.EUserType;
import com.mrmrscart.helpandsupportservice.entity.HelpSupport;
import com.mrmrscart.helpandsupportservice.entity.HelpSupportMessage;
import com.mrmrscart.helpandsupportservice.entity.HelpSupportMessageMedia;
import com.mrmrscart.helpandsupportservice.entity.HelpSupportMessageView;
import com.mrmrscart.helpandsupportservice.exception.HelpSupportException;
import com.mrmrscart.helpandsupportservice.exception.InvalidUserTypeException;
import com.mrmrscart.helpandsupportservice.exception.TicketNotFoundException;
import com.mrmrscart.helpandsupportservice.pojo.HelpSupportFilterPojo;
import com.mrmrscart.helpandsupportservice.pojo.HelpSupportMessagePojo;
import com.mrmrscart.helpandsupportservice.pojo.HelpSupportMessageViewPojo;
import com.mrmrscart.helpandsupportservice.pojo.HelpSupportPojo;
import com.mrmrscart.helpandsupportservice.pojo.TicketIssueTypeCountPojo;
import com.mrmrscart.helpandsupportservice.pojo.TicketStatusCountPojo;
import com.mrmrscart.helpandsupportservice.repository.HelpSupportRepository;
import com.mrmrscart.helpandsupportservice.util.PaginatedResponse;

@Service
public class HelpSupportServiceImpl implements HelpSupportService {

	private final List<EUserType> userTypeList = Arrays.asList(EUserType.CUSTOMER, EUserType.SUPPLIER,
			EUserType.RESELLER);
	private final List<ETicketStatus> ticketStatusList = Arrays.asList(ETicketStatus.OPEN, ETicketStatus.PENDING,
			ETicketStatus.CLOSED, ETicketStatus.ACTIVE);

	@Autowired
	private HelpSupportRepository helpSupportRepository;

	@Override
	public HelpSupport createTicket(HelpSupportPojo helpSupportPojo) {
		HelpSupport helpSupport = new HelpSupport();
		List<HelpSupportMessage> helpSupportMessages = new ArrayList<>();
		BeanUtils.copyProperties(helpSupportPojo, helpSupport);
		helpSupport.setTicketStatus(ETicketStatus.PENDING.name());

		helpSupportPojo.getHelpSupportMessagePojos().forEach(pojo -> {
			List<HelpSupportMessageMedia> helpSupportMessageMedias = new ArrayList<>();
			HelpSupportMessage message = new HelpSupportMessage();
			message.setMessageFromId(pojo.getMessageFromId());
			message.setMessageFromType(pojo.getMessageFromType());
			message.setMessage(pojo.getMessage());
			message.setMessagedAt(LocalDateTime.now());
			helpSupportPojo.getMediaUrl().forEach(media -> {
				HelpSupportMessageMedia messageMedia = new HelpSupportMessageMedia();
				messageMedia.setMediaUrl(media);
				helpSupportMessageMedias.add(messageMedia);
			});
			message.setHelpSupportMessageMedias(helpSupportMessageMedias);
			helpSupportMessages.add(message);
		});
		helpSupport.setHelpSupportMessages(helpSupportMessages);
		return helpSupportRepository.save(helpSupport);
	}

	@Override
	public List<HelpSupport> getAllSupplierTickets(int pageNumber, int pageSize, HelpSupportFilterPojo pojo) {
		try {
			if (pojo.getTicketType() == EUserType.ADMIN) {
				return getAdminTickets(pageNumber, pageSize, pojo);
			} else if (pojo.getTicketType() == EUserType.CUSTOMER) {
				return getCustomerTickets(pageNumber, pageSize, pojo);
			} else {
				throw new InvalidUserTypeException("Provide A Valid User Type");
			}
		} catch (InvalidUserTypeException | TicketNotFoundException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HelpSupportException(SOMETHING_WENT_WRONG);
		}
	}

	@SuppressWarnings("unchecked")
	private List<HelpSupport> getAdminTickets(int pageNumber, int pageSize, HelpSupportFilterPojo pojo) {
		List<HelpSupport> support;
		try {
			if (pojo.getFilterType() != null && pojo.getFilterType() == EFilterType.ISSUE_TYPE
					&& pojo.getKeyword() != null) {

				support = helpSupportRepository.findByUserFromIdAndIsDeletedAndIssueTypeIgnoreCaseContaining(
						pojo.getUserId(), false, pojo.getKeyword());
				support.addAll(
						helpSupportRepository.findByUserToIdAndUserFromTypeAndIsDeletedAndIssueTypeIgnoreCaseContaining(
								pojo.getUserId(), EUserType.ADMIN, false, pojo.getKeyword()));

			} else if (pojo.getFilterType() != null && pojo.getFilterType() == (EFilterType.TICKET_STATUS)) {

				support = helpSupportRepository.findByUserFromIdAndIsDeletedAndTicketStatusIgnoreCaseContaining(
						pojo.getUserId(), false, pojo.getKeyword());
				support.addAll(helpSupportRepository
						.findByUserToIdAndUserFromTypeAndIsDeletedAndTicketStatusIgnoreCaseContaining(pojo.getUserId(),
								EUserType.ADMIN, false, pojo.getKeyword()));

			} else if (pojo.getFilterType() != null && pojo.getFilterType() == EFilterType.TICKET_ID) {

				support = helpSupportRepository.filterUserTickets(pojo.getUserId(), false, pojo.getKeyword());
				support.addAll(helpSupportRepository.filterAdminTickets(pojo.getUserId(), EUserType.ADMIN, false,
						pojo.getKeyword()));

			} else {

				support = helpSupportRepository.findByUserFromIdAndIsDeleted(pojo.getUserId(), false);
				support.addAll(helpSupportRepository.findByUserToIdAndUserFromTypeAndIsDeleted(pojo.getUserId(),
						EUserType.ADMIN, false));
			}
			return (List<HelpSupport>) PaginatedResponse.getPaginatedResponse(support, pageNumber, pageSize);
		} catch (TicketNotFoundException e) {
			throw new TicketNotFoundException(GET_TICKET_FAILURE_MESSAGE);
		}
	}

	@SuppressWarnings("unchecked")
	private List<HelpSupport> getCustomerTickets(int pageNumber, int pageSize, HelpSupportFilterPojo pojo) {
		List<HelpSupport> support;
		try {
			if (pojo.getFilterType() != null && pojo.getFilterType() == EFilterType.ISSUE_TYPE
					&& pojo.getKeyword() != null) {

				support = helpSupportRepository
						.findByUserFromTypeAndUserToIdAndIsDeletedAndTicketStatusIgnoreCaseContaining(
								EUserType.CUSTOMER, pojo.getUserId(), false, pojo.getKeyword());

			} else if (pojo.getFilterType() != null && pojo.getFilterType() == (EFilterType.TICKET_STATUS)) {

				support = helpSupportRepository
						.findByUserFromTypeAndUserToIdAndIsDeletedAndTicketStatusIgnoreCaseContaining(
								EUserType.CUSTOMER, pojo.getUserId(), false, pojo.getKeyword());

			} else if (pojo.getFilterType() != null && pojo.getFilterType() == EFilterType.TICKET_ID) {

				support = helpSupportRepository.filterCustomerTickets(EUserType.CUSTOMER, pojo.getUserId(), false,
						pojo.getKeyword());

			} else {

				support = helpSupportRepository.findByUserFromTypeAndUserToIdAndIsDeleted(EUserType.CUSTOMER,
						pojo.getUserId(), false);

			}
			return (List<HelpSupport>) PaginatedResponse.getPaginatedResponse(support, pageNumber, pageSize);
		} catch (TicketNotFoundException e) {
			throw new TicketNotFoundException(GET_TICKET_FAILURE_MESSAGE);
		}
	}

	@Override
	public boolean viewTicket(HelpSupportMessageViewPojo messageViewPojo) {
		Optional<HelpSupport> optional = helpSupportRepository.findById(messageViewPojo.getTicketId());
		if (optional.isPresent()) {
			optional.get().getHelpSupportMessages().forEach(message -> {
				List<HelpSupportMessageView> messageViews = new ArrayList<>();
				HelpSupportMessageView view = new HelpSupportMessageView();
				view.setViewedById(messageViewPojo.getViewedById());
				view.setViewedByType(messageViewPojo.getViewedByType());
				view.setViewedDateTime(LocalDateTime.now());
				messageViews.add(view);
				message.setHelpSupportMessageViews(messageViews);
			});
			helpSupportRepository.save(optional.get());
			return true;
		} else {
			throw new HelpSupportException("No Ticket Found");
		}
	}

	@Override
	public boolean sendReply(HelpSupportMessagePojo helpSupportMessagePojo) {
		Optional<HelpSupport> ticket = helpSupportRepository.findById(helpSupportMessagePojo.getTicketId());
		if (ticket.isPresent()) {
			List<HelpSupportMessage> messages = new ArrayList<>();
			HelpSupportMessage message = new HelpSupportMessage();
			message.setMessage(helpSupportMessagePojo.getMessage());
			message.setMessageFromId(helpSupportMessagePojo.getMessageFromId());
			message.setMessageFromType(helpSupportMessagePojo.getMessageFromType());
			message.setMessagedAt(LocalDateTime.now());
			message.setHelpSupportMessageMedias(helpSupportMessagePojo.getImageUrlList());
			messages.add(message);
			ticket.get().setHelpSupportMessages(messages);
			ticket.get().setTicketStatus(ETicketStatus.OPEN.name());
			helpSupportRepository.save(ticket.get());
			return true;
		} else {
			throw new HelpSupportException("No Ticket Found");
		}
	}

	/*
	 * TICKET_STATUS 1.CLOSED, 2.ACTIVE(Open + Pending), 3.PENDING
	 */

	private TicketStatusCountPojo getAllTicketCountByUserType(EUserType userType) {
		long pendingTicketCount = 0;
		long closedTicketCount = 0;
		long activeTicketCount = 0;
		try {
			List<HelpSupport> ticketList = helpSupportRepository.findByUserFromType(userType);
			pendingTicketCount = ticketList.stream()
					.filter(e -> e.getTicketStatus().equalsIgnoreCase(ETicketStatus.PENDING.name())).count();

			activeTicketCount = ticketList.stream()
					.filter(e -> e.getTicketStatus().equalsIgnoreCase(ETicketStatus.PENDING.name())
							|| e.getTicketStatus().equalsIgnoreCase(ETicketStatus.OPEN.name()))
					.count();

			closedTicketCount = ticketList.stream()
					.filter(e -> e.getTicketStatus().equalsIgnoreCase(ETicketStatus.CLOSED.name())).count();
			return new TicketStatusCountPojo(userType, activeTicketCount, pendingTicketCount, closedTicketCount);
		} catch (Exception e) {
			throw new HelpSupportException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<TicketStatusCountPojo> getDashboardData() {
		try {
			List<TicketStatusCountPojo> ticketList = new ArrayList<>();
			userTypeList.forEach(e -> ticketList.add(getAllTicketCountByUserType(e)));
			return ticketList;
		} catch (Exception e) {
			throw new HelpSupportException(SOMETHING_WENT_WRONG);
		}
	}

	private TicketIssueTypeCountPojo getTicketCountByIssueType(EUserType userFromType, ETicketStatus ticketStatus) {
		long orderRelatedIssueCount = 0;
		long paymentTransactionRelatedIssue = 0;
		long returnAndRefundCount = 0;
		long logisticsRelatedIssueCount = 0;
		long cancellationAndRefundCount = 0;
		long profileRelatedIssueCount = 0;
		long paymentSettlementIssueCount = 0;
		long othersCount = 0;
		List<HelpSupport> ticketList = helpSupportRepository.findByUserFromTypeAndTicketStatus(userFromType,
				ticketStatus.name());
		try {
			orderRelatedIssueCount = ticketList.stream()
					.filter(e -> e.getIssueType().equals(EIssueType.ORDER_RELATED_ISSUE.name())).count();
			returnAndRefundCount = ticketList.stream()
					.filter(e -> e.getIssueType().equals(EIssueType.RETURN_AND_REFUND.name())).count();
			logisticsRelatedIssueCount = ticketList.stream()
					.filter(e -> e.getIssueType().equals(EIssueType.LOGISTICS_RELATED_ISSUE.name())).count();
			cancellationAndRefundCount = ticketList.stream()
					.filter(e -> e.getIssueType().equals(EIssueType.CANCELLATION_AND_REFUND.name())).count();
			profileRelatedIssueCount = ticketList.stream()
					.filter(e -> e.getIssueType().equals(EIssueType.PROFILE_RELATED_ISSUE.name())).count();
			paymentSettlementIssueCount = ticketList.stream()
					.filter(e -> e.getIssueType().equals(EIssueType.PAYMENT_SETTLEMENT_ISSUE.name())).count();
			othersCount = ticketList.stream().filter(e -> e.getIssueType().equals(EIssueType.OTHERS.name())).count();
			return new TicketIssueTypeCountPojo(userFromType, ticketStatus, orderRelatedIssueCount,
					paymentTransactionRelatedIssue, returnAndRefundCount, logisticsRelatedIssueCount,
					cancellationAndRefundCount, profileRelatedIssueCount, paymentSettlementIssueCount, othersCount);
		} catch (Exception e) {
			e.printStackTrace();
			throw new HelpSupportException(SOMETHING_WENT_WRONG);
		}
	}

	/**
	 * ORDER_RELATED_ISSUE, RETURN_AND_REFUND, LOGISTICS_RELATED_ISSUE,
	 * CANCELLATION_AND_REFUND, PROFILE_RELATED_ISSUE, PAYMENT_SETTLEMENT_ISSUE,
	 * OTHERS
	 */
	@Override
	public List<TicketIssueTypeCountPojo> getTicketCountByIssueTypeAndStatus(EUserType userFromType) {
		List<TicketIssueTypeCountPojo> ticketList = new ArrayList<>();
		try {
			ticketStatusList.forEach(e -> {
				if (e.name().equals(ETicketStatus.ACTIVE.name())) {
					TicketIssueTypeCountPojo ticketObjOne = getTicketCountByIssueType(userFromType, ETicketStatus.OPEN);
					TicketIssueTypeCountPojo ticketObjTwo = getTicketCountByIssueType(userFromType,
							ETicketStatus.PENDING);
					ticketList.add(new TicketIssueTypeCountPojo(userFromType, e,
							ticketObjOne.getOrderRelatedIssueCount() + ticketObjTwo.getOrderRelatedIssueCount(),
							ticketObjOne.getPaymentTransactionRelatedIssue()
									+ ticketObjTwo.getPaymentTransactionRelatedIssue(),
							ticketObjOne.getReturnAndRefundCount() + ticketObjTwo.getReturnAndRefundCount(),
							ticketObjOne.getLogisticRelatedIssueCount() + ticketObjTwo.getLogisticRelatedIssueCount(),
							ticketObjOne.getCancellationAndRefundCount() + ticketObjTwo.getCancellationAndRefundCount(),
							ticketObjOne.getProfileRelatedIssueCount() + ticketObjTwo.getProfileRelatedIssueCount(),
							ticketObjOne.getPaymentSettlementIssueCount()
									+ ticketObjTwo.getPaymentSettlementIssueCount(),
							ticketObjOne.getOthersCount() + ticketObjTwo.getOthersCount()));
				} else {
					ticketList.add(getTicketCountByIssueType(userFromType, e));
				}
			});
			return ticketList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HelpSupportException(SOMETHING_WENT_WRONG);
		}
	}

}
