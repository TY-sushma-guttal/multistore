package com.mrmrscart.notificationreportlogservice.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mrmrscart.notificationreportlogservice.entity.EStatus;
import com.mrmrscart.notificationreportlogservice.entity.MarketingToolNotification;
import com.mrmrscart.notificationreportlogservice.exception.NoCustomerFoundException;
import com.mrmrscart.notificationreportlogservice.exception.NotificationNotFoundException;
import com.mrmrscart.notificationreportlogservice.feign.UserService;
import com.mrmrscart.notificationreportlogservice.pojo.MarketingToolNotificationFilterPojo;
import com.mrmrscart.notificationreportlogservice.pojo.MarketingToolNotificationPojo;
import com.mrmrscart.notificationreportlogservice.pojo.MarketingToolNotificationViewPojo;
import com.mrmrscart.notificationreportlogservice.repository.MarketingToolNotificationRespository;
import com.mrmrscart.notificationreportlogservice.response.CustomerResponse;

import feign.FeignException;

@Service
public class MarketingToolNotificationServiceImpl implements MarketingToolNotificationService {

	@Autowired
	MarketingToolNotificationRespository marketingToolNotificationRespository;

	@Autowired
	private UserService userService;

	@Override
	public MarketingToolNotification addMarketingToolNotification(
			MarketingToolNotificationPojo marketingToolNotificationPojo) {
		try {
			if (marketingToolNotificationPojo.getMarketingToolNotificationId() != null) {
				MarketingToolNotification toolNotification = marketingToolNotificationRespository
						.findByMarketingToolNotificationIdAndIsDelete(
								marketingToolNotificationPojo.getMarketingToolNotificationId(), false);
				if (toolNotification != null) {
					toolNotification.setScheduledDateTime(marketingToolNotificationPojo.getScheduledDateTime());
					toolNotification.setStatus(EStatus.SCHEDULED);
					return marketingToolNotificationRespository.save(toolNotification);
				} else {
					throw new NotificationNotFoundException("Invalid notification Id");
				}
			} else {
				MarketingToolNotification marketingToolNotification = new MarketingToolNotification();
				BeanUtils.copyProperties(marketingToolNotificationPojo, marketingToolNotification,
						"marketingToolNotificationId");
				if (marketingToolNotificationPojo.getScheduledDateTime() != null)
					marketingToolNotification.setStatus(EStatus.SCHEDULED);
				else {
					marketingToolNotification.setStatus(EStatus.SENT);
				}
				marketingToolNotification.setType(marketingToolNotificationPojo.getType().name());
				return marketingToolNotificationRespository.save(marketingToolNotification);
			}
		} catch (Exception e) {
			throw new NotificationNotFoundException("Something Went Wrong");
		}
	}

	@Override
	public boolean deleteMarketingToolNotification(Long marketingToolNotificationId) {
		try {
			Optional<MarketingToolNotification> optional = marketingToolNotificationRespository
					.findById(marketingToolNotificationId);
			if (optional.isPresent()) {
				optional.get().setDelete(true);
				marketingToolNotificationRespository.save(optional.get());
				return true;
			} else {
				throw new NotificationNotFoundException("Invalid notification Id");
			}
		} catch (Exception e) {
			throw new NotificationNotFoundException("Something Went Wrong");
		}
	}

	@Override
	public List<MarketingToolNotificationViewPojo> getAllMarketingToolNotifications(String supplierId) {
		try {
			List<MarketingToolNotificationViewPojo> viewPojos = new ArrayList<>();
			marketingToolNotificationRespository.findBySupplierIdAndIsDelete(supplierId, false).forEach(tool -> {
				MarketingToolNotificationViewPojo viewPojo = new MarketingToolNotificationViewPojo();
				BeanUtils.copyProperties(tool, viewPojo);

				ResponseEntity<CustomerResponse> customers = userService.getCustomers(tool.getCustomerIds());
				CustomerResponse body = customers.getBody();
				if (body != null && !body.getData().isEmpty()) {
					viewPojo.setCustomers(body.getData());

				} else {
					throw new NoCustomerFoundException("No Customer Found");
				}

				viewPojos.add(viewPojo);
			});
			return viewPojos;
		} catch (Exception e) {
			throw new NotificationNotFoundException("Something Went Wrong");
		}
	}

	@Override
	public List<MarketingToolNotificationViewPojo> getPaginatedMarketingToolNotifications(int pageNumber, int pageSize,
			String supplierId, MarketingToolNotificationFilterPojo filterPojo) {
		try {
			List<MarketingToolNotificationViewPojo> viewPojos = new ArrayList<>();
			List<MarketingToolNotification> notifications;
			if (filterPojo.getKeyword() != null) {
				notifications = marketingToolNotificationRespository
						.findBySupplierIdAndIsDeleteAndNotificationTitleContaining(supplierId, false,
								filterPojo.getKeyword());
				notifications.forEach(tool -> {
					MarketingToolNotificationViewPojo viewPojo = new MarketingToolNotificationViewPojo();
					BeanUtils.copyProperties(tool, viewPojo);

					ResponseEntity<CustomerResponse> customers = userService.getCustomers(tool.getCustomerIds());
					CustomerResponse body = customers.getBody();
					if (body != null && !body.getData().isEmpty()) {
						viewPojo.setCustomers(body.getData());

					} else {
						throw new NoCustomerFoundException("No Customer Found");
					}

					viewPojos.add(viewPojo);
				});
			} else {
				notifications = marketingToolNotificationRespository.findBySupplierIdAndIsDelete(supplierId, false);
				notifications.forEach(tool -> {
					MarketingToolNotificationViewPojo viewPojo = new MarketingToolNotificationViewPojo();
					BeanUtils.copyProperties(tool, viewPojo);

					ResponseEntity<CustomerResponse> customers = userService.getCustomers(tool.getCustomerIds());
					CustomerResponse body = customers.getBody();
					if (body != null && !body.getData().isEmpty()) {
						viewPojo.setCustomers(body.getData());

					} else {
						throw new NoCustomerFoundException("No Customer Found");
					}

					viewPojos.add(viewPojo);
				});
			}
			Collections.sort(viewPojos, (b, a) -> a.getLastModifiedDate().compareTo(b.getLastModifiedDate()));
			return getPaginatedResponse(filterPojo.getDateFrom(), filterPojo.getDateTo(), pageNumber, pageSize,
					viewPojos);
		} catch (FeignException e) {
			throw e;
		} catch (Exception e) {
			throw new NoCustomerFoundException("Something Went Wrong");
		}
	}

	private List<MarketingToolNotificationViewPojo> getPaginatedResponse(LocalDateTime dateFrom, LocalDateTime dateTo,
			int pageNumber, int pageSize, List<MarketingToolNotificationViewPojo> find) {
		List<MarketingToolNotificationViewPojo> list = new ArrayList<>();
		if (dateFrom != null && dateTo != null) {
			list = find.stream()
					.filter(f -> dateFrom.isBefore(f.getCreatedDate()) && dateTo.isAfter(f.getCreatedDate()))
					.collect(Collectors.toList());
			if (list.isEmpty()) {
				return list;
			}
		}

		if (pageNumber == 0) {
			if (!list.isEmpty())
				return list.stream().limit(pageSize).collect(Collectors.toList());
			else
				return find.stream().limit(pageSize).collect(Collectors.toList());
		} else {
			int skipCount = (pageNumber) * pageSize;
			if (!list.isEmpty())
				return list.stream().skip(skipCount).limit(pageSize).collect(Collectors.toList());
			else
				return find.stream().skip(skipCount).limit(pageSize).collect(Collectors.toList());
		}
	}

	@Override
	public MarketingToolNotification sendNotification(Long marketingToolNotificationId) {
		try {
			MarketingToolNotification toolNotification = marketingToolNotificationRespository
					.findByMarketingToolNotificationIdAndIsDelete(marketingToolNotificationId, false);
			if (toolNotification != null) {
				toolNotification.setScheduledDateTime(LocalDateTime.now());
				toolNotification.setStatus(EStatus.SENT);
				return marketingToolNotificationRespository.save(toolNotification);
			} else {
				throw new NotificationNotFoundException("Invalid notification Id");
			}
		} catch (Exception e) {
			throw new NotificationNotFoundException("Something Went Wrong");
		}
	}

	@Override
	public MarketingToolNotification getNotificationById(Long marketingToolNotificationId) {
		MarketingToolNotification notification = marketingToolNotificationRespository
				.findByMarketingToolNotificationIdAndIsDelete(marketingToolNotificationId, false);
		if (notification != null) {
			return notification;
		} else {
			throw new NotificationNotFoundException("Notification Not Found");
		}
	}

}
