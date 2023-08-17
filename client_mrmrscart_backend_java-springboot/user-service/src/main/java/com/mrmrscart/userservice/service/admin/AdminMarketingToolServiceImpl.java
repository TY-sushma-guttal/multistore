package com.mrmrscart.userservice.service.admin;

import static com.mrmrscart.userservice.common.admin.AdminMarketingToolsConstant.ADMIN_MARKETING_TOOL_CAMPAIGN_GET_FAIL_MESSAGE;
import static com.mrmrscart.userservice.common.admin.AdminMarketingToolsConstant.ADMIN_MARKETING_TOOL_EXIST;
import static com.mrmrscart.userservice.common.admin.AdminMarketingToolsConstant.ADMIN_MARKETING_TOOL_GET_FAIL_MESSAGE;
import static com.mrmrscart.userservice.common.admin.AdminMarketingToolsConstant.INVALID_ID;
import static com.mrmrscart.userservice.common.admin.AdminMarketingToolsConstant.INVALID_TOOL_STATUS;
import static com.mrmrscart.userservice.common.admin.AdminMarketingToolsConstant.MARKETING_TOOL_NOT_FOUND;
import static com.mrmrscart.userservice.common.admin.AdminMarketingToolsConstant.SOMETHING_WENT_WRONG;
import static com.mrmrscart.userservice.common.admin.AdminMarketingToolsConstant.SUBSCRIPTION_NOT_ACTIVE_MESSAGE;
import static com.mrmrscart.userservice.common.admin.AdminMarketingToolsConstant.SUBSCRIPTION_NOT_FOUND_MESSAGE;
import static com.mrmrscart.userservice.common.admin.AdminMarketingToolsConstant.TOOL_EXPIRED_MESSAGE;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mrmrscart.userservice.audit.BaseConfigController;
import com.mrmrscart.userservice.entity.admin.AdminMarketingTools;
import com.mrmrscart.userservice.entity.admin.AdminMarketingToolsCampaign;
import com.mrmrscart.userservice.entity.admin.EMarketingToolStatus;
import com.mrmrscart.userservice.entity.admin.EMarketingToolType;
import com.mrmrscart.userservice.entity.admin.EMarketingTools;
import com.mrmrscart.userservice.entity.admin.EStoreType;
import com.mrmrscart.userservice.entity.reseller.ActiveMarketingToolSubscription;
import com.mrmrscart.userservice.entity.reseller.EMarketingToolCommentsType;
import com.mrmrscart.userservice.entity.reseller.MarketingToolPurchaseHistory;
import com.mrmrscart.userservice.entity.reseller.UserMarketingTool;
import com.mrmrscart.userservice.entity.supplier.EUserRole;
import com.mrmrscart.userservice.exception.admin.AdminMarketingToolNotFoundException;
import com.mrmrscart.userservice.exception.admin.AdminMarketingToolsCampaignNotFoundException;
import com.mrmrscart.userservice.exception.admin.AdminMarketingToolsException;
import com.mrmrscart.userservice.exception.admin.MarketingToolStatusException;
import com.mrmrscart.userservice.exception.admin.MarketingToolSubscriptionNotFoundException;
import com.mrmrscart.userservice.exception.admin.ToolSubscriptionExpiredException;
import com.mrmrscart.userservice.exception.reseller.IndividualPricingNotFoundException;
import com.mrmrscart.userservice.exception.reseller.MarketingToolException;
import com.mrmrscart.userservice.exception.reseller.PurchaseHistorySaveFailedException;
import com.mrmrscart.userservice.feign.client.NotificationReportLogService;
import com.mrmrscart.userservice.feign.pojo.MarketingToolPriceChangePojo;
import com.mrmrscart.userservice.pojo.admin.MarketingToolPurchaseHistoryPojo;
import com.mrmrscart.userservice.pojo.admin.AdminMarketingToolCountPojo;
import com.mrmrscart.userservice.pojo.admin.AdminMarketingToolDisableEnablePojo;
import com.mrmrscart.userservice.pojo.admin.AdminMarketingToolsPojo;
import com.mrmrscart.userservice.pojo.admin.AdminMarketingToolsPricePojo;
import com.mrmrscart.userservice.pojo.admin.IndividualPricingPojo;
import com.mrmrscart.userservice.pojo.admin.MarketingToolApprovalPojo;
import com.mrmrscart.userservice.pojo.admin.MarketingToolSubscriptionPojo;
import com.mrmrscart.userservice.pojo.admin.SubscriptionCountPojo;
import com.mrmrscart.userservice.pojo.reseller.MarketingToolCommentPojo;
import com.mrmrscart.userservice.repository.admin.AdminMarketingToolsCampaignRepository;
import com.mrmrscart.userservice.repository.admin.AdminMarketingToolsRepository;
import com.mrmrscart.userservice.repository.reseller.ActiveMarketingToolSubscriptionRepository;
import com.mrmrscart.userservice.repository.reseller.MarketingToolPurchaseHistoryRepository;
import com.mrmrscart.userservice.repository.reseller.UserMarketingToolRepository;
import com.mrmrscart.userservice.response.supplier.SuccessResponse;

@Service
public class AdminMarketingToolServiceImpl extends BaseConfigController implements AdminMarketingToolService {

	@Autowired
	private AdminMarketingToolsRepository adminMarketingToolsRepository;
	@Autowired
	private MarketingToolPurchaseHistoryRepository purchasedMarketingToolsRepository;

	@Autowired
	private ActiveMarketingToolSubscriptionRepository activeMarketingToolSubscriptionRepository;
	@Autowired
	private UserMarketingToolRepository userMarketingToolRepository;

	@Autowired
	private NotificationReportLogService notificationReportLogService;

	@Autowired
	private AdminMarketingToolsCampaignRepository adminMarketingToolsCampaignRepository;

	@Override
	public AdminMarketingTools addIndividualPricing(AdminMarketingToolsPojo adminMarketingToolsPojo) {

		try {
			AdminMarketingTools adminMarketingTools = adminMarketingToolsRepository
					.findByAdminMarketingToolNameAndDaysAndStoreType(
							adminMarketingToolsPojo.getAdminMarketingToolName().name(),
							adminMarketingToolsPojo.getDays(), adminMarketingToolsPojo.getStoreType().name());
			if (adminMarketingTools == null) {
				AdminMarketingTools adminMarketingTools2 = new AdminMarketingTools();
				BeanUtils.copyProperties(adminMarketingToolsPojo, adminMarketingTools2);
				adminMarketingTools2
						.setAdminMarketingToolName(adminMarketingToolsPojo.getAdminMarketingToolName().name());
				adminMarketingTools2.setDays(adminMarketingToolsPojo.getDays());
				adminMarketingTools2.setStoreType(adminMarketingToolsPojo.getStoreType().name());
				if (LocalDateTime.now().isBefore(adminMarketingToolsPojo.getStartDateTime())) {
					adminMarketingTools2.setStatus(EMarketingToolStatus.YET_TO_START);
				} else {
					adminMarketingTools2.setStatus(EMarketingToolStatus.ACTIVE);
				}
				return adminMarketingToolsRepository.save(adminMarketingTools2);
			} else {
				throw new AdminMarketingToolsException(ADMIN_MARKETING_TOOL_EXIST);
			}
		} catch (AdminMarketingToolsException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AdminMarketingToolsException(SOMETHING_WENT_WRONG);
		}

	}

	@Override
	public List<IndividualPricingPojo> getIndividualPricing(EUserRole userRole) {

		try {
			List<AdminMarketingTools> findByStoreType = adminMarketingToolsRepository.findByStoreType(userRole.name());

			if (findByStoreType.isEmpty()) {
				throw new AdminMarketingToolsException(ADMIN_MARKETING_TOOL_GET_FAIL_MESSAGE);
			} else {
				return findIndividualPricingDetails(findByStoreType);
			}

		} catch (Exception e) {
			throw new AdminMarketingToolsException(ADMIN_MARKETING_TOOL_GET_FAIL_MESSAGE);
		}
	}

	private List<IndividualPricingPojo> findIndividualPricingDetails(List<AdminMarketingTools> adminMarketingTools) {
		List<String> statusList = new ArrayList<>();

		statusList.addAll(Arrays.asList("DISCOUNT_COUPON", "TODAYS_DEAL", "SPIN_WHEEL", "SCRATCH_CARD",
				"PRICE_TARGETED", "NOTIFICATIONS", "FLAGS", "QUIZ"));

		List<IndividualPricingPojo> result = new ArrayList<>();

		statusList.forEach(e -> {
			IndividualPricingPojo individualPricingPojo = new IndividualPricingPojo();
			List<AdminMarketingTools> arrayList = new ArrayList<>();
			arrayList = adminMarketingTools.stream().filter(f -> f.getAdminMarketingToolName().equals(e))
					.collect(Collectors.toList());
			individualPricingPojo.setToolName(e);
			individualPricingPojo.setAdminMarketingTools(arrayList);
			result.add(individualPricingPojo);
		});

		return result;
	}

	@Transactional
	@Override
	public UserMarketingTool approveRejectMarketingTool(EMarketingToolStatus status, Long marketingToolId,
			String userId) {
		try {
			UserMarketingTool findById = findToolByStatus(marketingToolId);

			if (status == EMarketingToolStatus.APPROVED) {

				checkSubscriptionValidity(findById.getToolType(), userId);

				if (LocalDateTime.now().isBefore(findById.getStartDateTime())) {
					findById.setToolStatus(EMarketingToolStatus.YET_TO_START);
					return userMarketingToolRepository.save(findById);

				} else {
					findById.setToolStatus(EMarketingToolStatus.ACTIVE);
					return userMarketingToolRepository.save(findById);
				}

			} else if (status == EMarketingToolStatus.REJECTED) {
				findById.setToolStatus(status);
				return userMarketingToolRepository.save(findById);
			} else {
				throw new MarketingToolStatusException(INVALID_TOOL_STATUS);
			}
		} catch (AdminMarketingToolsException | ToolSubscriptionExpiredException
				| MarketingToolSubscriptionNotFoundException | MarketingToolStatusException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new MarketingToolException(SOMETHING_WENT_WRONG);
		}
	}

	private void checkSubscriptionValidity(EMarketingTools marketingTool, String subscriptionById) {
		ActiveMarketingToolSubscription marketingToolSubscription = activeMarketingToolSubscriptionRepository
				.findByMarketingToolNameAndSubscriptionById(marketingTool.name(), subscriptionById);

		if (marketingToolSubscription != null) {

			if (LocalDateTime.now().isAfter(marketingToolSubscription.getExpirationDate())) {
				throw new ToolSubscriptionExpiredException(TOOL_EXPIRED_MESSAGE);
			}
		} else {
			throw new MarketingToolSubscriptionNotFoundException(SUBSCRIPTION_NOT_FOUND_MESSAGE);
		}
	}

	private UserMarketingTool findToolByStatus(Long marketingToolId) {
		try {
			UserMarketingTool findById = userMarketingToolRepository.findByMarketingToolIdAndToolStatusAndIsDelete(marketingToolId,
					EMarketingToolStatus.PENDING,false);
			if (findById != null) {
				return findById;
			} else {
				throw new AdminMarketingToolsException(MARKETING_TOOL_NOT_FOUND);
			}
		} catch (AdminMarketingToolsException e) {
			throw new AdminMarketingToolsException(MARKETING_TOOL_NOT_FOUND);

		}
	}

	@Override
	public List<MarketingToolApprovalPojo> getAllUnapprovedMarketingTools(int pageNumber, int pageSize,
			EMarketingTools marketingToolType) {
		List<MarketingToolApprovalPojo> marketingToolApprovalPojos = new ArrayList<>();
		userMarketingToolRepository
				.findByToolTypeAndToolStatusAndIsDelete(marketingToolType, EMarketingToolStatus.PENDING, false)
				.forEach(tool -> {
					MarketingToolApprovalPojo pojo = new MarketingToolApprovalPojo();
					BeanUtils.copyProperties(tool, pojo);
					marketingToolApprovalPojos.add(pojo);
				});
		return getMarketingToolApprovalPage(pageNumber, pageSize, marketingToolApprovalPojos);
	}

	private List<MarketingToolApprovalPojo> getMarketingToolApprovalPage(int pageNumber, int pageSize,
			List<MarketingToolApprovalPojo> find) {

		if (pageNumber == 0) {
			return find.stream().limit(pageSize).collect(Collectors.toList());
		} else {
			int skipCount = (pageNumber) * pageSize;
			return find.stream().skip(skipCount).limit(pageSize).collect(Collectors.toList());
		}
	}

	@Override
	@Transactional
	public AdminMarketingTools updatePrice(AdminMarketingToolsPricePojo adminMarketingToolsPricePojo) {
		try {
			Optional<AdminMarketingTools> findById = adminMarketingToolsRepository
					.findById(adminMarketingToolsPricePojo.getToolId());
			if (findById.isPresent()) {
				AdminMarketingTools adminMarketingTools = findById.get();
				MarketingToolPriceChangePojo marketingToolPriceChangePojo = new MarketingToolPriceChangePojo(
						adminMarketingTools.getPrice(), adminMarketingToolsPricePojo.getPrice(),
						adminMarketingToolsPricePojo.getToolId(), EMarketingToolType.INDIVIDUAL_PRICING.name(),
						getUserId());
				adminMarketingTools.setPrice(adminMarketingToolsPricePojo.getPrice());
				AdminMarketingTools save = adminMarketingToolsRepository.save(adminMarketingTools);

				ResponseEntity<SuccessResponse> addMarketingToolPriceChange = notificationReportLogService
						.addMarketingToolPriceChange(marketingToolPriceChangePojo);
				if (addMarketingToolPriceChange.getStatusCode() != HttpStatus.OK) {
					throw new AdminMarketingToolsException(SOMETHING_WENT_WRONG);
				}
				return save;
			} else {
				throw new AdminMarketingToolsException(MARKETING_TOOL_NOT_FOUND);
			}
		} catch (AdminMarketingToolsException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AdminMarketingToolsException(SOMETHING_WENT_WRONG);
		}
	}

	@Transactional
	@Override
	public boolean enableDisableToolSubscription(Long purchaseId, boolean status, EMarketingTools marketingTool) {
		try {
			MarketingToolPurchaseHistory findById = purchasedMarketingToolsRepository
					.findByPurchaseIdAndIsDisabledAndToolStatus(purchaseId, !status, EMarketingToolStatus.ACTIVE);

			if (findById != null) {
				findById.setDisabled(status);
				purchasedMarketingToolsRepository.save(findById);

				ActiveMarketingToolSubscription subscription = activeMarketingToolSubscriptionRepository
						.findByMarketingToolNameAndSubscriptionById(marketingTool.name(), findById.getPurchasedById());

				if (subscription != null) {
					subscription.setDisabled(status);
					activeMarketingToolSubscriptionRepository.save(subscription);
					return status;
				} else {
					throw new MarketingToolSubscriptionNotFoundException(SUBSCRIPTION_NOT_ACTIVE_MESSAGE);
				}
			} else {
				throw new MarketingToolStatusException(SUBSCRIPTION_NOT_FOUND_MESSAGE);
			}
		} catch (MarketingToolStatusException | MarketingToolSubscriptionNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new MarketingToolException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<AdminMarketingTools> enableOrDisableTool(
			AdminMarketingToolDisableEnablePojo adminMarketingToolDisableEnablePojo) {
		try {
			List<AdminMarketingTools> findAllById = adminMarketingToolsRepository
					.findAllById(adminMarketingToolDisableEnablePojo.getToolIdList());
			if (findAllById.size() != adminMarketingToolDisableEnablePojo.getToolIdList().size()) {
				throw new AdminMarketingToolsException(INVALID_ID);
			}
			List<AdminMarketingTools> adminMarketingTools = new ArrayList<>();
			findAllById.stream().forEach(e -> {
				e.setDisabled(adminMarketingToolDisableEnablePojo.isDisabled());
				adminMarketingTools.add(e);
			});
			return adminMarketingToolsRepository.saveAll(adminMarketingTools);
		} catch (AdminMarketingToolsException e) {
			throw e;
		} catch (Exception e) {
			throw new AdminMarketingToolsException(SOMETHING_WENT_WRONG);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MarketingToolPurchaseHistory> getSubscriptionsByTool(MarketingToolSubscriptionPojo pojo, int pageNumber,
			int pageSize) {

		List<MarketingToolPurchaseHistory> purchaseHistories = new ArrayList<>();

		try {
			if (pojo.getToolStatus() == EMarketingToolStatus.ALL && (pojo.getUserType() == EUserRole.ALL)) {
				purchaseHistories = adminMarketingToolsRepository
						.getSubscriptionsByToolName(pojo.getMarketingTool().name());
				if (purchaseHistories.isEmpty()) {
					return purchaseHistories;

				} else
					return (List<MarketingToolPurchaseHistory>) getPaginatedResponse(purchaseHistories, pageNumber,
							pageSize);

			} else if (pojo.getToolStatus() != EMarketingToolStatus.ALL && (pojo.getUserType() == EUserRole.ALL)) {

				purchaseHistories = adminMarketingToolsRepository
						.getSubscriptionsByToolNameAndStatus(pojo.getMarketingTool().name(), pojo.getToolStatus());

				if (purchaseHistories.isEmpty()) {
					return purchaseHistories;

				} else
					return (List<MarketingToolPurchaseHistory>) getPaginatedResponse(purchaseHistories, pageNumber,
							pageSize);

			} else if (pojo.getToolStatus() == EMarketingToolStatus.ALL && (pojo.getUserType() != EUserRole.ALL)) {

				purchaseHistories = adminMarketingToolsRepository.getSubscriptionsByToolNameAndUserType(
						pojo.getMarketingTool().name(), pojo.getUserType().name());

				if (purchaseHistories.isEmpty()) {
					return purchaseHistories;

				} else
					return (List<MarketingToolPurchaseHistory>) getPaginatedResponse(purchaseHistories, pageNumber,
							pageSize);
			} else {
				purchaseHistories = adminMarketingToolsRepository.getSubscriptionsByToolNameAndToolStatusAndUserType(
						pojo.getMarketingTool().name(), pojo.getToolStatus(), pojo.getUserType().name());
				if (purchaseHistories.isEmpty()) {
					return purchaseHistories;

				} else
					return (List<MarketingToolPurchaseHistory>) getPaginatedResponse(purchaseHistories, pageNumber,
							pageSize);

			}
		} catch (Exception e) {
			throw new MarketingToolSubscriptionNotFoundException(SUBSCRIPTION_NOT_FOUND_MESSAGE);
		}
	}

	private List<?> getPaginatedResponse(List<?> purchaseHistories, int pageNumber, int pageSize) {

		if (pageNumber == 0) {
			return purchaseHistories.stream().limit(pageSize).collect(Collectors.toList());
		} else {
			int skipCount = (pageNumber) * pageSize;
			return purchaseHistories.stream().skip(skipCount).limit(pageSize).collect(Collectors.toList());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IndividualPricingPojo> getEnabledIndividualPricing(EUserRole storeType, int pageNumber, int pageSize) {
		try {
			List<AdminMarketingTools> adminMarketingTools = adminMarketingToolsRepository
					.findByStoreTypeAndIsDisabled(storeType.name(), false);
			if (adminMarketingTools.isEmpty()) {
				throw new IndividualPricingNotFoundException(ADMIN_MARKETING_TOOL_GET_FAIL_MESSAGE);
			} else {
				List<IndividualPricingPojo> individualPricingDetails = findIndividualPricingDetails(
						adminMarketingTools);
				if (individualPricingDetails.isEmpty()) {
					return new ArrayList<>();
				} else {
					return (List<IndividualPricingPojo>) getPaginatedResponse(individualPricingDetails, pageNumber,
							pageSize);
				}
			}
		} catch (IndividualPricingNotFoundException e) {
			throw e;
		} catch (MarketingToolException e) {
			throw new MarketingToolException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<SubscriptionCountPojo> getSubscriptionCount(EStoreType type) {
		List<AdminMarketingToolCountPojo> fetchMarketingToolPurchaseHistoryJoin = purchasedMarketingToolsRepository
				.fetchMarketingToolPurchaseHistoryJoin(type.name());
		List<String> toolNameList = Arrays.asList("DISCOUNT_COUPON", "TODAYS_DEAL", "SPIN_WHEEL", "SCRATCH_CARD",
				"PRICE_TARGETED", "NOTIFICATIONS", "FLAGS", "QUIZ");
		List<SubscriptionCountPojo> subscriptionCountPojos = new ArrayList<>();
		toolNameList.forEach(e -> {
			List<AdminMarketingToolCountPojo> totalCount = fetchMarketingToolPurchaseHistoryJoin.stream()
					.filter(f -> f.getAdminMarketingToolName().equals(e)).collect(Collectors.toList());
			Long activeCount = totalCount.stream().filter(f -> f.getToolStatus() == EMarketingToolStatus.ACTIVE)
					.count();
			SubscriptionCountPojo subscriptionCountPojo = new SubscriptionCountPojo();
			subscriptionCountPojo.setAdminMarketingToolName(e);
			subscriptionCountPojo.setTotalCount(totalCount.size());
			subscriptionCountPojo.setActiveCount(activeCount);
			subscriptionCountPojo.setInActiveCount(totalCount.size() - activeCount);
			subscriptionCountPojos.add(subscriptionCountPojo);
		});
		return subscriptionCountPojos;
	}

	@Override
	public Object addMarketingToolComments(MarketingToolCommentPojo marketingToolCommentPojo) {

		try {
			if (marketingToolCommentPojo.getType() == EMarketingToolCommentsType.INDIVIDUAL_PRICING) {
				Optional<AdminMarketingTools> findById = adminMarketingToolsRepository
						.findById(marketingToolCommentPojo.getTypeId());

				if (findById.isPresent()) {
					findById.get().setComments(marketingToolCommentPojo.getComments());
					findById.get().setCommentsAttachmentUrl(marketingToolCommentPojo.getCommentsAttachment());
					return adminMarketingToolsRepository.save(findById.get());
				} else {
					throw new AdminMarketingToolNotFoundException(ADMIN_MARKETING_TOOL_GET_FAIL_MESSAGE);
				}
			} else if (marketingToolCommentPojo.getType() == EMarketingToolCommentsType.TOOL_CAMPAIGN) {
				Optional<AdminMarketingToolsCampaign> findById = adminMarketingToolsCampaignRepository
						.findById(marketingToolCommentPojo.getTypeId());

				if (findById.isPresent()) {
					findById.get().setComments(marketingToolCommentPojo.getComments());
					findById.get().setCommentsAttachmentUrl(marketingToolCommentPojo.getCommentsAttachment());
					return adminMarketingToolsCampaignRepository.save(findById.get());

				} else {
					throw new AdminMarketingToolsCampaignNotFoundException(
							ADMIN_MARKETING_TOOL_CAMPAIGN_GET_FAIL_MESSAGE);
				}
			} else {
				Optional<MarketingToolPurchaseHistory> findById = purchasedMarketingToolsRepository
						.findById(marketingToolCommentPojo.getTypeId());
				if (findById.isPresent()) {
					findById.get().setComments(marketingToolCommentPojo.getComments());
					findById.get().setCommentsAttachment(marketingToolCommentPojo.getCommentsAttachment());
					return purchasedMarketingToolsRepository.save(findById.get());

				} else {
					throw new PurchaseHistorySaveFailedException(SUBSCRIPTION_NOT_FOUND_MESSAGE);
				}
			}
		} catch (AdminMarketingToolNotFoundException | AdminMarketingToolsCampaignNotFoundException
				| PurchaseHistorySaveFailedException e) {
			throw e;
		} catch (Exception e) {
			throw new MarketingToolException(SOMETHING_WENT_WRONG);
		}

	}

	@Override
	public MarketingToolPurchaseHistoryPojo getSubscription(String purchasedById, String purchasedByType,
			String adminMarketingToolName) {
		try {
			MarketingToolPurchaseHistoryPojo marketingToolSubscriptionPojo = new MarketingToolPurchaseHistoryPojo();
			MarketingToolPurchaseHistory purchaseHistory = purchasedMarketingToolsRepository
					.findByPurchasedByTypeAndPurchasedByIdAndAdminMarketingToolNameAndIsDisabledAndToolStatus(
							purchasedByType, purchasedById, adminMarketingToolName, false,
							EMarketingToolStatus.ACTIVE);
			if (purchaseHistory != null) {
				if (LocalDateTime.now().isAfter(purchaseHistory.getExpirationDate())) {
					throw new ToolSubscriptionExpiredException(TOOL_EXPIRED_MESSAGE);
				}
				BeanUtils.copyProperties(purchaseHistory, marketingToolSubscriptionPojo);
			} else {
				throw new MarketingToolSubscriptionNotFoundException(SUBSCRIPTION_NOT_FOUND_MESSAGE);
			}
			return marketingToolSubscriptionPojo;
		} catch (ToolSubscriptionExpiredException | MarketingToolSubscriptionNotFoundException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new MarketingToolException(SOMETHING_WENT_WRONG);
		}
	}
}
