package com.mrmrscart.userservice.service.reseller;

import static com.mrmrscart.userservice.common.reseller.MarketingToolThemeConstant.*;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrmrscart.userservice.entity.reseller.MarketingToolTheme;
import com.mrmrscart.userservice.exception.reseller.MarketingToolThemeException;
import com.mrmrscart.userservice.exception.reseller.MarketingToolThemeNotFound;
import com.mrmrscart.userservice.pojo.reseller.MarketingToolThemePojo;
import com.mrmrscart.userservice.repository.reseller.MarketingToolThemeRepository;

@Service
public class MarketingToolThemeServiceImpl implements MarketingToolThemeService {

	@Autowired
	private MarketingToolThemeRepository marketingToolThemeRepository;

	@Override
	public MarketingToolTheme addMarketingToolTheme(MarketingToolThemePojo data) {
		try {
			MarketingToolTheme marketingToolTheme = new MarketingToolTheme();
			BeanUtils.copyProperties(data, marketingToolTheme);
			return marketingToolThemeRepository.save(marketingToolTheme);
		} catch (Exception e) {
			throw new MarketingToolThemeException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public MarketingToolTheme getMarketingToolThemeById(Long id) {
		try {
			Optional<MarketingToolTheme> findById = marketingToolThemeRepository.findById(id);
			if (findById.isPresent()) {
				return findById.get();
			} else {
				throw new MarketingToolThemeNotFound(INVALID_MARKETING_TOOL_THEME_ID);
			}
		} catch (MarketingToolThemeNotFound e) {
			throw e;
		} catch (Exception e) {
			throw new MarketingToolThemeException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public List<MarketingToolTheme> getAllMarketingToolTheme() {
		return marketingToolThemeRepository.findAll();
	}
}
