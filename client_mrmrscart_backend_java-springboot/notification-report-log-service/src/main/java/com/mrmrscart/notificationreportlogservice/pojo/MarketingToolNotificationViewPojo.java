package com.mrmrscart.notificationreportlogservice.pojo;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mrmrscart.notificationreportlogservice.entity.ENotificationType;
import com.mrmrscart.notificationreportlogservice.entity.EStatus;
import com.mrmrscart.notificationreportlogservice.feign.pojo.DropDownPojo;

import lombok.Data;

@Data
public class MarketingToolNotificationViewPojo {

	private Long marketingToolNotificationId;
	private String supplierId;
	private String notificationTitle;
	private String notificationMessage;
	private List<String> attachmentUrl;
	private ENotificationType notificationType;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime scheduledDateTime;
	private List<DropDownPojo> customers;
	private EStatus status;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime lastModifiedDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime createdDate;
	private String type;
}
