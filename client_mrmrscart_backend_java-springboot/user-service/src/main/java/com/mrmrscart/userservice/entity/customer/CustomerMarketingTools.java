package com.mrmrscart.userservice.entity.customer;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.mrmrscart.userservice.entity.reseller.UserMarketingTool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_customer_marketing_tools")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerMarketingTools {

	@EmbeddedId
	private CustomerMarketingToolsKey id;

	@ManyToOne
	@MapsId("customerId")
	@JoinColumn(name = "customer_id")
	private CustomerRegistration customerRegistration;

	@ManyToOne
	@MapsId("marketingToolId")
	@JoinColumn(name = "marketing_tool_id")
	private UserMarketingTool resellerMarketingTool;

	private int usageCount;
}
