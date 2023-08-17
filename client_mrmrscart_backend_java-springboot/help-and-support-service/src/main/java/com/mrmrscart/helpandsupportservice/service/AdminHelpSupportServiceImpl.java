package com.mrmrscart.helpandsupportservice.service;

import static com.mrmrscart.helpandsupportservice.common.HelpSupportConstant.CLOSE_TICKET_FAILED;
import static com.mrmrscart.helpandsupportservice.common.HelpSupportConstant.DELETE_TICKET_FAILED;
import static com.mrmrscart.helpandsupportservice.common.HelpSupportConstant.SOMETHING_WENT_WRONG;
import static com.mrmrscart.helpandsupportservice.common.HelpSupportConstant.TICKET_NOT_FOUND;
import static com.mrmrscart.helpandsupportservice.util.HelpSupportSpecification.*;
import static org.springframework.data.jpa.domain.Specification.where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mrmrscart.helpandsupportservice.entity.EIssueType;
import com.mrmrscart.helpandsupportservice.entity.ETicketStatus;
import com.mrmrscart.helpandsupportservice.entity.EUserType;
import com.mrmrscart.helpandsupportservice.entity.HelpSupport;
import com.mrmrscart.helpandsupportservice.entity.HelpSupportMessage;
import com.mrmrscart.helpandsupportservice.entity.HelpSupportMessageMedia;
import com.mrmrscart.helpandsupportservice.exception.HelpSupportException;
import com.mrmrscart.helpandsupportservice.exception.TicketNotFoundException;
import com.mrmrscart.helpandsupportservice.feign.client.ProductServiceClient;
import com.mrmrscart.helpandsupportservice.feign.client.UserServiceClient;
import com.mrmrscart.helpandsupportservice.feign.pojo.UserDetailsResponseWrapper;
import com.mrmrscart.helpandsupportservice.feign.pojo.UserInfoWrapper;
import com.mrmrscart.helpandsupportservice.feign.response.UserDetailsResponse;
import com.mrmrscart.helpandsupportservice.pojo.AdminHelpSupportFilterPojo;
import com.mrmrscart.helpandsupportservice.pojo.HelpSupportPojo;
import com.mrmrscart.helpandsupportservice.pojo.SupplierQueryPojo;
import com.mrmrscart.helpandsupportservice.repository.HelpSupportRepository;
import com.mrmrscart.helpandsupportservice.util.PaginatedResponse;
import com.mrmrscart.helpandsupportservice.wrapper.AdminHelpSupportFilterWrapper;
import com.mrmrscart.helpandsupportservice.wrapper.HelpSupportQueryWrapper;
import com.mrmrscart.helpandsupportservice.wrapper.HelpSupportWrapper;

import feign.FeignException;

@Transactional
@Service
public class AdminHelpSupportServiceImpl implements AdminHelpSupportService {

	@Autowired
	private HelpSupportRepository helpSupportRepository;

	@Autowired
	private UserServiceClient userServiceClient;

	@Autowired
	private ProductServiceClient productServiceClient;

	@Transactional
	@Override
	public boolean deleteTicket(Long ticketId) {
		try {
			HelpSupport helpSupport = findTicket(ticketId);
			if (helpSupport.getTicketStatus().equals(ETicketStatus.CLOSED.name())) {
				helpSupport.setDeleted(true);
				helpSupportRepository.save(helpSupport);
				return false;
			} else {
				throw new TicketNotFoundException(DELETE_TICKET_FAILED);
			}
		} catch (TicketNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new HelpSupportException(SOMETHING_WENT_WRONG);
		}
	}

	private HelpSupport findTicket(Long ticketId) {
		try {
			HelpSupport helpSupport = helpSupportRepository.findByTicketIdAndIsDeleted(ticketId, false);
			if (helpSupport != null) {
				return helpSupport;
			} else {
				throw new TicketNotFoundException(TICKET_NOT_FOUND);
			}
		} catch (Exception e) {
			throw new TicketNotFoundException(TICKET_NOT_FOUND);
		}
	}

	@Transactional
	@Override
	public boolean closeTicket(Long ticketId) {
		try {
			HelpSupport findTicket = findTicket(ticketId);
			if (findTicket.getTicketStatus().equals(ETicketStatus.CLOSED.name())) {
				throw new TicketNotFoundException(CLOSE_TICKET_FAILED);
			} else {
				findTicket.setTicketStatus(ETicketStatus.CLOSED.name());
				helpSupportRepository.save(findTicket);
				return false;
			}
		} catch (TicketNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new HelpSupportException(SOMETHING_WENT_WRONG);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<HelpSupportWrapper> getTicketsByUserType(AdminHelpSupportFilterPojo filterPojo, int pageNumber,
			int pageSize) {
		List<HelpSupportWrapper> result = new ArrayList<>();
		try {
			List<HelpSupport> helpSupports = helpSupportRepository.findAll(filterTickets(filterPojo));

			if (helpSupports.isEmpty()) {
				return result;

			} else {
				List<String> list = helpSupports.stream().map(HelpSupport::getUserFromId).collect(Collectors.toList());
				List<UserDetailsResponseWrapper> userNames = getUserNames(filterPojo.getUserType(), list);

				Set<UserDetailsResponseWrapper> hashSet = new HashSet<>();
				hashSet.addAll(userNames);

				helpSupports.forEach(e -> hashSet.forEach(f -> {
					if (e.getUserFromId().equals(f.getUserId())) {
						HelpSupportWrapper helpSupportWrapper = new HelpSupportWrapper();
						BeanUtils.copyProperties(e, helpSupportWrapper);
						helpSupportWrapper.setUserFromName(f.getUserName());
						result.add(helpSupportWrapper);
					}
				}));

				return (List<HelpSupportWrapper>) PaginatedResponse.getPaginatedResponse(
						result.stream().sorted(Comparator.comparing(HelpSupportWrapper::getLastModifiedDate).reversed())
								.collect(Collectors.toList()),
						pageNumber, pageSize);
			}
		} catch (TicketNotFoundException | FeignException e) {
			throw e;
		} catch (Exception e) {
			throw new HelpSupportException(SOMETHING_WENT_WRONG);
		}
	}

	private Specification<HelpSupport> filterTicketsByUserType(AdminHelpSupportFilterPojo helpSupportFilterPojo) {
		if (helpSupportFilterPojo.getUserType() == EUserType.CUSTOMER_STORE) {
			return where(findByUserFromType(EUserType.CUSTOMER).and(findByIsDeleted(false))
					.and(findByUserToType(EUserType.SUPPLIER)));
		} else
			return where(findByUserFromType(helpSupportFilterPojo.getUserType()).and(where(findByIsDeleted(false)))
					.and(findByUserToType(EUserType.ADMIN)))
					.or(findByUserFromType(EUserType.ADMIN)
							.and(findByUserToType(helpSupportFilterPojo.getUserType()).and(findByIsDeleted(false))));
	}

	private Specification<HelpSupport> filterTickets(AdminHelpSupportFilterPojo helpSupportFilterPojo) {

		Specification<HelpSupport> specification = filterTicketsByUserType(helpSupportFilterPojo);

		if (!helpSupportFilterPojo.getTicketStatus().isEmpty()) {
			specification = specification.and(where(containsTicketStatus(helpSupportFilterPojo.getTicketStatus())));
		}
		if (!helpSupportFilterPojo.getIssueType().isEmpty()) {
			specification = specification.and(where(containsIssueType(helpSupportFilterPojo.getIssueType())));
		}
		if (!helpSupportFilterPojo.getTicketId().isEmpty()) {
			specification = specification.and(where(containsTicketId(helpSupportFilterPojo.getTicketId())));
		}
		return specification;
	}

	private List<UserDetailsResponseWrapper> getUserNames(EUserType userType, List<String> userId) {
		UserInfoWrapper userInfoWrapper = new UserInfoWrapper(userId, userType);
		try {
			ResponseEntity<UserDetailsResponse> responseEntity = userServiceClient.getUserDetails(userInfoWrapper);
			UserDetailsResponse body = responseEntity.getBody();
			if (body != null) {
				if (!body.getData().isEmpty()) {
					return body.getData();
				} else {
					throw new TicketNotFoundException(TICKET_NOT_FOUND);
				}
			} else {
				throw new TicketNotFoundException(TICKET_NOT_FOUND);
			}
		} catch (FeignException e) {
			throw e;
		} catch (Exception e) {
			throw new HelpSupportException(TICKET_NOT_FOUND);
		}
	}

	@Override
	public List<AdminHelpSupportFilterWrapper> getFilterInfo(EUserType userType) {
		List<AdminHelpSupportFilterWrapper> result = new ArrayList<>();
		try {
			AdminHelpSupportFilterWrapper adminHelpSupport = new AdminHelpSupportFilterWrapper("status", Arrays
					.asList(ETicketStatus.OPEN.name(), ETicketStatus.PENDING.name(), ETicketStatus.CLOSED.name()));
			result.add(adminHelpSupport);

			AdminHelpSupportFilterWrapper adminHelpSupport2 = new AdminHelpSupportFilterWrapper("issue type",
					Arrays.asList(EIssueType.CANCELLATION_AND_REFUND.name(), EIssueType.LOGISTICS_RELATED_ISSUE.name(),
							EIssueType.ORDER_RELATED_ISSUE.name(), EIssueType.PAYMENT_SETTLEMENT_ISSUE.name(),
							EIssueType.PROFILE_RELATED_ISSUE.name(), EIssueType.RETURN_AND_REFUND.name(),
							EIssueType.OTHERS.name()));
			result.add(adminHelpSupport2);

			AdminHelpSupportFilterPojo adminHelpSupportFilterPojo = new AdminHelpSupportFilterPojo(new ArrayList<>(),
					new ArrayList<>(), new ArrayList<>(), userType);

			List<HelpSupport> findAll = helpSupportRepository
					.findAll(filterTicketsByUserType(adminHelpSupportFilterPojo));

			AdminHelpSupportFilterWrapper adminHelpSupport3 = new AdminHelpSupportFilterWrapper("ticket id",
					findAll.stream().map(e -> e.getTicketId().toString()).collect(Collectors.toList()));
			result.add(adminHelpSupport3);

			return result;
		} catch (Exception e) {
			throw new HelpSupportException(SOMETHING_WENT_WRONG);
		}
	}

	@Transactional
	@Override
	public HelpSupport createProductTickets(HelpSupportPojo helpSupportPojo) {
		try {
			HelpSupport helpSupport = new HelpSupport();
			BeanUtils.copyProperties(helpSupportPojo, helpSupport);
			List<HelpSupportMessage> helpSupportMessages = new ArrayList<>();
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
			HelpSupport support = helpSupportRepository.save(helpSupport);
			productServiceClient.setProductStatus(helpSupportPojo.getProductVariationId());
			return support;
		} catch (FeignException e) {
			throw e;
		} catch (Exception e) {
			throw new HelpSupportException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<HelpSupportQueryWrapper> getSupplierQueries(SupplierQueryPojo pojo, int pageNumber, int pageSize) {
		List<HelpSupportQueryWrapper> result = new ArrayList<>();
		try {
			List<HelpSupport> helpSupport = helpSupportRepository.findAll(filterQueryTickets(pojo));
			if (helpSupport.isEmpty()) {
				return Collections.emptyList();
			} else {
				List<String> collect = helpSupport.stream().map(HelpSupport::getUserToId).collect(Collectors.toList());
				List<UserDetailsResponseWrapper> userNames = getUserNames(EUserType.SUPPLIER, collect);
				Set<UserDetailsResponseWrapper> set = new HashSet<>();
				set.addAll(userNames);
				helpSupport.forEach(e -> set.forEach(f -> {
					if (e.getUserToId().equals(f.getUserId())) {
						HelpSupportQueryWrapper wrapper = new HelpSupportQueryWrapper();
						BeanUtils.copyProperties(e, wrapper);
						wrapper.setSupplierId(f.getUserId());
						result.add(wrapper);
					}
				}));
				return result;
			}
		} catch (FeignException e) {
			throw e;
		} catch (Exception e) {
			throw new HelpSupportException(SOMETHING_WENT_WRONG);
		}
	}

	private Specification<HelpSupport> filterQueryTickets(SupplierQueryPojo pojo) {
		Specification<HelpSupport> specification = where(
				containsIssueType(Arrays.asList(EIssueType.ACCOUNT_MANAGEMENT_ISSUE.name()))
						.and(findByIsDeleted(false)));
		if (!pojo.getTicketStatus().isEmpty()) {
			specification = specification.and(where(containsTicketStatus(pojo.getTicketStatus())));
		}
		if (pojo.getFromDate() != null && pojo.getToDate() != null) {
			specification.and(where(findByFromDateAndToDate(pojo.getFromDate(), pojo.getToDate())));
		}
		return specification;
	}
}
