package com.mrmrscart.userservice.entity.admin;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mrmrscart.userservice.audit.Audit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_admin_marketing_tools_campaign")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AdminMarketingToolsCampaign extends Audit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long adminMarketingToolsCampaignId;

	@Column(length = 20)
	private String title;

	private String days;

	private BigDecimal price;

	private boolean isDisabled;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime startDateTime;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime endDateTime;

	private String storeType;

	private EMarketingToolStatus status;

	@Column(length = 90)
	private String comments;

	@Column(length = 255)
	private String commentsAttachmentUrl;

	@ManyToMany(cascade = CascadeType.ALL)
	private List<AdminMarketingTools> adminMarketingTools;
}
