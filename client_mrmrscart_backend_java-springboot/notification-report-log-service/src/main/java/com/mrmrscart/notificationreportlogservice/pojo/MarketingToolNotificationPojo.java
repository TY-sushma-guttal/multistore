package com.mrmrscart.notificationreportlogservice.pojo;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mrmrscart.notificationreportlogservice.entity.ECustomerNotificationType;
import com.mrmrscart.notificationreportlogservice.entity.ENotificationType;

import lombok.Data;

@Data
public class MarketingToolNotificationPojo {

	private Long marketingToolNotificationId;
	private String supplierId;
	private String notificationTitle;
	private String notificationMessage;
	private List<String> attachmentUrl;
	private ENotificationType notificationType;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime scheduledDateTime;
	private List<String> customerIds;
	private ECustomerNotificationType type;

}
