package com.mrmrscart.notificationreportlogservice.service;

import static com.mrmrscart.notificationreportlogservice.common.MarketingToolPriceChangeConstant.SOMETHING_WENT_WRONG;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrmrscart.notificationreportlogservice.entity.MarketingToolPriceChange;
import com.mrmrscart.notificationreportlogservice.exception.MarketingToolPriceChangeException;
import com.mrmrscart.notificationreportlogservice.pojo.MarketingToolPriceChangePojo;
import com.mrmrscart.notificationreportlogservice.pojo.PriceChangeHistoryPojo;
import com.mrmrscart.notificationreportlogservice.repository.MarketingToolPriceChangeRepository;

@Service
public class MarketingToolPriceChangeServiceImpl implements MarketingToolPriceChangeService {

	@Autowired
	private MarketingToolPriceChangeRepository marketingToolPriceChangeRepository;

	@Override
	public MarketingToolPriceChange addMarketingToolPriceChange(
			MarketingToolPriceChangePojo marketingToolPriceChangePojo) {
		try {
			MarketingToolPriceChange marketingToolPriceChange = new MarketingToolPriceChange();
			BeanUtils.copyProperties(marketingToolPriceChangePojo, marketingToolPriceChange);

			marketingToolPriceChange.setCurrentRate(marketingToolPriceChangePojo.getNewPrice());

			marketingToolPriceChange
					.setDescription("Price Was Changed From " + marketingToolPriceChangePojo.getOldPrice().doubleValue()
							+ " To " + marketingToolPriceChangePojo.getNewPrice().doubleValue());

			marketingToolPriceChange.setCreatedAt(LocalDateTime.now());
			return marketingToolPriceChangeRepository.save(marketingToolPriceChange);
		} catch (Exception e) {
			throw new MarketingToolPriceChangeException(SOMETHING_WENT_WRONG);
		}
	}

	@Transactional
	@Override
	public List<MarketingToolPriceChange> viewPriceChangeHistory(PriceChangeHistoryPojo priceChangeHistoryPojo,
			int pageNumber, int pageSize) {
		try {
			List<MarketingToolPriceChange> list = marketingToolPriceChangeRepository.findByToolTypeAndToolIdIn(
					priceChangeHistoryPojo.getType().name(), priceChangeHistoryPojo.getToolId());
			if (list.isEmpty()) {
				return list;
			}
			return getPaginatedResponse(list, pageNumber, pageSize);
		} catch (Exception e) {
			throw new MarketingToolPriceChangeException(SOMETHING_WENT_WRONG);
		}
	}

	private List<MarketingToolPriceChange> getPaginatedResponse(List<MarketingToolPriceChange> storeCoupons,
			int pageNumber, int pageSize) {
		if (pageNumber == 0) {
			return storeCoupons.stream().limit(pageSize).collect(Collectors.toList());
		} else {
			int skipCount = (pageNumber) * pageSize;
			return storeCoupons.stream().skip(skipCount).limit(pageSize).collect(Collectors.toList());
		}
	}

}
