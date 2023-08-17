package com.mrmrscart.helpandsupportservice.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_help_support_message_view")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("serial")
public class HelpSupportMessageView implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long viewId;
	@Column(length = 20)
	private EUserType viewedByType;
	@Column(length = 45)
	private String viewedById;
	private LocalDateTime viewedDateTime;
	private LocalDateTime updatedAt;
}
