package com.mrmrscart.userservice.entity.reseller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mrmrscart.userservice.entity.admin.EMarketingToolStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_marketing_tool_purchase_history")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketingToolPurchaseHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long purchaseId;

	@Column(length = 25)
	private String purchasedByType;

	@Column(length = 45)
	private String purchasedById;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime purchasedAt;

	private EMarketingToolStatus toolStatus;

	private BigDecimal subscriptionAmount;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime activatedAt;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime expirationDate;
	/* campaign or individual */
	private String subscriptionType;

	/* thats id */
	private Long subscriptionTypeId;

	private boolean isDisabled;

	@Column(length = 255)
	private String comments;

	@Column(length = 255)
	private String commentsAttachment;

	private String orderId;

	private String adminMarketingToolName;

	private String days;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "purchase_id")
	private List<UserMarketingTool> userMarketingTools;

}
