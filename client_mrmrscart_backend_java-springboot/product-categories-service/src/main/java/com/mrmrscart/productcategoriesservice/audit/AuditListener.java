package com.mrmrscart.productcategoriesservice.audit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;


@Configuration
public class AuditListener extends BaseConfigController {
	
	 @Bean
	    public AuditorAware<String> auditorAware() {
	        return new EntityAuditorAware();
	    }

}
