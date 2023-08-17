package com.mrmrscart.userservice.entity.reseller;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mrmrscart.userservice.entity.admin.EMarketingToolStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_active_marketing_tool_subscription")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveMarketingToolSubscription {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long subscriptionId;
	
	@Column(length = 25)
	/*supplier*/
	private String subscriptionByType;
	
	@Column(length = 45)
	/*SUP12326547*/
	private String subscriptionById;
	
	private String days;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime activatedAt;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime expirationDate;

	private EMarketingToolStatus subscriptionStatus;
	
	@Column(length = 50)
	private String marketingToolName;
	
	private boolean isDisabled;
}
