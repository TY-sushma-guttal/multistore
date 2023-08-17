package com.mrmrscart.productcategoriesservice.config;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.mrmrscart.productcategoriesservice.entity.category.EStatus;
import com.mrmrscart.productcategoriesservice.entity.product.ProductFlag;
import com.mrmrscart.productcategoriesservice.repository.product.ProductFlagRepository;

@Configuration
@EnableScheduling
public class SchedulerConfig {

	
	@Autowired
	ProductFlagRepository productFlagRepository;


	@Scheduled(cron= "0 10 * * * *")
	public void checkProductFlagExpiry() {
		List<ProductFlag> flags = productFlagRepository.findAll();
		List<ProductFlag> list = flags.stream().filter(f->LocalDateTime.now().compareTo(f.getEndDateTime())>0).collect(Collectors.toList());
		if(!list.isEmpty()) {
			list.forEach(l->{
				l.setFlagStatus(EStatus.INACTIVE);
				productFlagRepository.save(l);
			});
		}
	}
	
}
