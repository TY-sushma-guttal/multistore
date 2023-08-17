package com.mrmrscart.productcategoriesservice.service.category;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mrmrscart.productcategoriesservice.entity.category.StandardOption;
import com.mrmrscart.productcategoriesservice.entity.category.StandardVariation;
import com.mrmrscart.productcategoriesservice.exception.category.StandardVariationException;
import com.mrmrscart.productcategoriesservice.pojo.category.StandardOptionPojo;
import com.mrmrscart.productcategoriesservice.repository.category.StandardOptionRepository;
import com.mrmrscart.productcategoriesservice.repository.category.StandardVariationRepository;
import static com.mrmrscart.productcategoriesservice.common.category.StandardVariationConstant.*;
@Service
public class StandardOptionServiceImpl implements StandardOptionService{
	
	@Autowired
	private StandardOptionRepository standardOptionRepository;
	
	@Autowired
	private StandardVariationRepository standardVariationRepository;

	@Override
	public List<StandardOption> addStandardOption(StandardOptionPojo standardOptionPojo) {
		Optional<StandardVariation> findById = standardVariationRepository.findById(standardOptionPojo.getStandardVariationId());
		if(findById.isPresent()) {
			List<StandardOption> standardOptions=new ArrayList<>();
			standardOptionPojo.getOptionName().forEach(e -> {
				StandardOption standardOption=new StandardOption();
				BeanUtils.copyProperties(standardOptionPojo, standardOption);
				standardOption.setOptionName(e);
				standardOption.setCreatedAt(LocalDateTime.now());
				standardOptions.add(standardOption);
			});
		
		return standardOptionRepository.saveAll(standardOptions);
		}else {
			throw new StandardVariationException(INVALID_STANDARD_VARIATION_ID);
		}
		 
	}

}
