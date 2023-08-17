package com.mrmrscart.userservice.service.admin;

import static com.mrmrscart.userservice.common.admin.AdminMarketingToolsConstant.COUNT_FAILURE;
import static com.mrmrscart.userservice.common.admin.AdminMarketingToolsConstant.EMPTY_LIST;
import static com.mrmrscart.userservice.common.admin.AdminMarketingToolsConstant.INVALID_MARKETING_TOOL_CAMPAIGN;
import static com.mrmrscart.userservice.common.admin.AdminMarketingToolsConstant.SOMETHING_WENT_WRONG;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mrmrscart.userservice.entity.admin.AdminMarketingTools;
import com.mrmrscart.userservice.entity.admin.AdminMarketingToolsCampaign;
import com.mrmrscart.userservice.entity.admin.EMarketingToolStatus;
import com.mrmrscart.userservice.entity.admin.EStoreType;
import com.mrmrscart.userservice.exception.admin.AdminMarketingToolFilterException;
import com.mrmrscart.userservice.exception.admin.AdminMarketingToolsCampaignException;
import com.mrmrscart.userservice.exception.admin.AdminMarketingToolsCampaignNotFoundException;
import com.mrmrscart.userservice.exception.admin.AdminMarketingToolsException;
import com.mrmrscart.userservice.exception.admin.FilterConditionException;
import com.mrmrscart.userservice.feign.client.NotificationReportLogService;
import com.mrmrscart.userservice.feign.pojo.MarketingToolPriceChangePojo;
import com.mrmrscart.userservice.pojo.admin.AdminMarketingToolsCampaignPojo;
import com.mrmrscart.userservice.pojo.admin.AdminMarketingToolsCampaignResponsePojo;
import com.mrmrscart.userservice.pojo.admin.AdminMarketingToolsCampaignUpdatePojo;
import com.mrmrscart.userservice.pojo.admin.AdminToolCampaignIdAndStatusPojo;
import com.mrmrscart.userservice.pojo.reseller.AdminCampaignToolFilterPojo;
import com.mrmrscart.userservice.repository.admin.AdminMarketingToolsCampaignRepository;
import com.mrmrscart.userservice.repository.admin.AdminMarketingToolsRepository;
import com.mrmrscart.userservice.response.supplier.SuccessResponse;
import com.mrmrscart.userservice.wrapper.admin.AdminMarketingToolsWrapper;

import feign.FeignException;

@Service
public class AdminMarketingToolsCampaignServiceImpl implements AdminMarketingToolsCampaignService {

	@Autowired
	private AdminMarketingToolsRepository adminMarketingToolsRepository;

	@Autowired
	private AdminMarketingToolsCampaignRepository adminMarketingToolsCampaignRepository;

	@Autowired
	private NotificationReportLogService notificationReportLogServiceClient;

	@Override
	public List<AdminMarketingToolsWrapper> getAdminMarketingTools(String days, EStoreType storeType) {
		List<AdminMarketingTools> list = adminMarketingToolsRepository.findByDaysAndStoreType(days, storeType.name());
		List<AdminMarketingToolsWrapper> marketingToolsWrappers = new ArrayList<>();
		list.forEach(e -> {
			AdminMarketingToolsWrapper adminMarketingToolsWrapper = new AdminMarketingToolsWrapper();
			BeanUtils.copyProperties(e, adminMarketingToolsWrapper);
			marketingToolsWrappers.add(adminMarketingToolsWrapper);
		});
		return marketingToolsWrappers;
	}

	@Override
	public AdminMarketingToolsCampaign createAdminMarketingToolCampaign(
			AdminMarketingToolsCampaignPojo adminMarketingToolsCampaignPojo) {

		try {
			List<AdminMarketingTools> findAllById = adminMarketingToolsRepository
					.findAllById(adminMarketingToolsCampaignPojo.getAdminMarketingToolIds());
			if (findAllById.size() < 2) {
				throw new AdminMarketingToolsCampaignException(COUNT_FAILURE);
			} else {
				AdminMarketingToolsCampaign adminMarketingToolsCampaign = new AdminMarketingToolsCampaign();
				BeanUtils.copyProperties(adminMarketingToolsCampaignPojo, adminMarketingToolsCampaign);
				adminMarketingToolsCampaign.setStoreType(adminMarketingToolsCampaignPojo.getStoreType().name());
				adminMarketingToolsCampaign.setAdminMarketingTools(findAllById);
				if (LocalDateTime.now().isBefore(adminMarketingToolsCampaignPojo.getStartDateTime())) {
					adminMarketingToolsCampaign.setStatus(EMarketingToolStatus.YET_TO_START);
				} else {
					adminMarketingToolsCampaign.setStatus(EMarketingToolStatus.ACTIVE);
				}
				return adminMarketingToolsCampaignRepository.save(adminMarketingToolsCampaign);
			}
		} catch (AdminMarketingToolsCampaignException e) {
			throw e;
		} catch (Exception e) {
			throw new AdminMarketingToolsCampaignException(SOMETHING_WENT_WRONG);
		}
	}

	private boolean notifyInNotificationService(AdminMarketingToolsCampaign oldMarketingToolCampaign, AdminMarketingToolsCampaignUpdatePojo data) {
		MarketingToolPriceChangePojo pojo =new MarketingToolPriceChangePojo();
		pojo.setCreatedBy(data.getStoreType().name());
		pojo.setNewPrice(data.getPrice());
		pojo.setOldPrice(oldMarketingToolCampaign.getPrice());
		pojo.setToolId(data.getAdminMarketingToolsCampaignId());
		pojo.setToolType("TOOL_CAMPAIGN");
		ResponseEntity<SuccessResponse> addMarketingToolPriceChange = notificationReportLogServiceClient.addMarketingToolPriceChange(pojo);
		if(addMarketingToolPriceChange.getBody()!=null) {
			return true;
		}
		return false;
	}
	
	@Override
	public AdminMarketingToolsCampaign updateCampaignTool(AdminMarketingToolsCampaignUpdatePojo data) {
		try {
			boolean flag=false;
			Optional<AdminMarketingToolsCampaign> findById = adminMarketingToolsCampaignRepository
					.findById(data.getAdminMarketingToolsCampaignId());
			if (findById.isPresent()) {
				if (data.getStoreType() != null) {
					findById.get().setStoreType(data.getStoreType().name());
				} else if (data.getStartDateTime() != null) {
					/* Start date ADMIN can edit */
					findById.get().setStartDateTime(data.getStartDateTime());
				} else if (data.getEndDateTime() != null) {
					findById.get().setEndDateTime(data.getEndDateTime());
				} else if (data.getPrice().doubleValue() > 0) {
					findById.get().setPrice(data.getPrice());
					flag=true;
				} else if (data.getTitle() != null) {
					findById.get().setTitle(data.getTitle());
				}
				AdminMarketingToolsCampaign saveMarketingToolCampaign = adminMarketingToolsCampaignRepository.save(findById.get());
				if(flag) {
					notifyInNotificationService(findById.get(),data);
				}
				return saveMarketingToolCampaign;
			} else {
				throw new AdminMarketingToolsCampaignNotFoundException(INVALID_MARKETING_TOOL_CAMPAIGN);
			}
		} catch (FeignException | AdminMarketingToolsCampaignNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new AdminMarketingToolsCampaignException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public AdminMarketingToolsCampaign enableDisableCampaign(AdminToolCampaignIdAndStatusPojo data) {
		try {
			Optional<AdminMarketingToolsCampaign> findById = adminMarketingToolsCampaignRepository
					.findById(data.getAdminToolCampaignId());
			if (findById.isPresent()) {
				findById.get().setDisabled(data.isStatus());
				return adminMarketingToolsCampaignRepository.save(findById.get());
			} else {
				throw new AdminMarketingToolsCampaignNotFoundException(INVALID_MARKETING_TOOL_CAMPAIGN);
			}
		} catch (AdminMarketingToolsCampaignNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new AdminMarketingToolsCampaignException(SOMETHING_WENT_WRONG);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AdminMarketingToolsCampaign> getAllAdminCampaignTool(String storeType, int pageNumber, int pageSize) {
		try {
			List<AdminMarketingToolsCampaign> findByStoreType = adminMarketingToolsCampaignRepository
					.findByStoreType(storeType);
			if (!findByStoreType.isEmpty()) {
				return (List<AdminMarketingToolsCampaign>) getPaginatedResponse(findByStoreType, pageNumber, pageSize);
			} else {
				throw new AdminMarketingToolsCampaignNotFoundException(EMPTY_LIST);
			}
		} catch (AdminMarketingToolsCampaignNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new AdminMarketingToolsCampaignException(SOMETHING_WENT_WRONG);
		}
	}

	private List<?> getPaginatedResponse(List<?> toolCampaignList, int pageNumber, int pageSize) {
		if (pageNumber == 0) {
			return toolCampaignList.stream().limit(pageSize).collect(Collectors.toList());
		} else {
			int skipCount = (pageNumber) * pageSize;
			return toolCampaignList.stream().skip(skipCount).limit(pageSize).collect(Collectors.toList());
		}
	}

	@Override
	public List<AdminMarketingToolsCampaign> getFilteredAdminCampaignTool(AdminCampaignToolFilterPojo data) {
		try {
			/* field name can be days,price,status */
			List<AdminMarketingToolsCampaign> campaignList = new ArrayList<>();
			if (data.getFieldName().equalsIgnoreCase("days")) {
				List<AdminMarketingToolsCampaign> filteredCampaignList = adminMarketingToolsCampaignRepository
						.findByDays(data.getFieldValue());
				campaignList.addAll(filteredCampaignList);
			} else if (data.getFieldName().equalsIgnoreCase("price")) {
				/* Frontend will send the value in the format of 100-200 */
				String[] split = data.getFieldValue().split("-");
				BigDecimal priceStart = new BigDecimal(split[0]);
				BigDecimal priceEnd = new BigDecimal(split[1]);
				if (priceStart.compareTo(priceEnd) == 0 || priceStart.compareTo(priceEnd) < 0) {
					throw new FilterConditionException("Price Range Is Not Proper");
				} else {
					List<AdminMarketingToolsCampaign> filteredCampaignList = adminMarketingToolsCampaignRepository
							.findByPriceRange(priceStart, priceEnd);
					campaignList.addAll(filteredCampaignList);
				}
			} else if (data.getFieldName().equalsIgnoreCase("status")) {
				List<AdminMarketingToolsCampaign> filteredCampaignList = adminMarketingToolsCampaignRepository
						.findByStatus(data.getStatus());
				campaignList.addAll(filteredCampaignList);
			} else {
				throw new AdminMarketingToolFilterException("Invalid Filter Condition");
			}
			return campaignList;
		} catch (AdminMarketingToolFilterException | FilterConditionException e) {
			throw e;
		} catch (Exception e) {
			throw new AdminMarketingToolsException(SOMETHING_WENT_WRONG);
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<AdminMarketingToolsCampaignResponsePojo> getEnabledAdminMarketingToolCampaign(String days,
			String storeType, int pageNumber, int pageSize) {
		try {
			List<AdminMarketingToolsCampaignResponsePojo> adminMarketingToolsCampaignResponsePojos = new ArrayList<>();
			List<AdminMarketingToolsCampaign> adminMarketingToolsCampaigns = new ArrayList<>();
			if (days != null) {
				adminMarketingToolsCampaigns = adminMarketingToolsCampaignRepository
						.findByStoreTypeAndStatusAndDays(storeType, EMarketingToolStatus.ACTIVE, days);
			} else if (days == null) {
				adminMarketingToolsCampaigns = adminMarketingToolsCampaignRepository.findByStoreTypeAndStatus(storeType,
						EMarketingToolStatus.ACTIVE);
			}
			List<AdminMarketingToolsCampaign> paginatedList = (List<AdminMarketingToolsCampaign>) getPaginatedResponse(
					adminMarketingToolsCampaigns, pageNumber, pageSize);
			paginatedList.forEach(e -> {
				AdminMarketingToolsCampaignResponsePojo adminMarketingToolsCampaignResponsePojo = new AdminMarketingToolsCampaignResponsePojo();
				BeanUtils.copyProperties(e, adminMarketingToolsCampaignResponsePojo);
				adminMarketingToolsCampaignResponsePojo.setExpiryDuration(
						java.time.temporal.ChronoUnit.DAYS.between(LocalDateTime.now(), e.getEndDateTime()) + " days ");
				adminMarketingToolsCampaignResponsePojo.setAdminMarketingTools(e.getAdminMarketingTools().stream()
						.map(AdminMarketingTools::getAdminMarketingToolName).collect(Collectors.toList()));
				adminMarketingToolsCampaignResponsePojos.add(adminMarketingToolsCampaignResponsePojo);
			});
			return adminMarketingToolsCampaignResponsePojos;
		} catch (Exception e) {
			throw new AdminMarketingToolsCampaignException(SOMETHING_WENT_WRONG);
		}
	}
}
