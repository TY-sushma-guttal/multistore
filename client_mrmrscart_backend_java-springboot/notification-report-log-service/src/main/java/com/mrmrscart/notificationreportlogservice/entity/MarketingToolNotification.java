package com.mrmrscart.notificationreportlogservice.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mrmrscart.notificationreportlogservice.audit.Audit;
import com.mrmrscart.notificationreportlogservice.util.ListToStringConverter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_marketing_tool_notification")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MarketingToolNotification extends Audit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long marketingToolNotificationId;
	@Column(length = 50)
	private String notificationTitle;
	@Column(length = 1020)
	private String notificationMessage;
	@Column(length = 255)
	@Convert(converter = ListToStringConverter.class)
	private List<String> attachmentUrl;
	private ENotificationType notificationType;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime scheduledDateTime;
	private String supplierId;
	@Convert(converter = ListToStringConverter.class)
	private List<String> customerIds;
	private EStatus status;
	private String type;
	private boolean isDelete;
}
