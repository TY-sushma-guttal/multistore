package com.mrmrscart.helpandsupportservice.wrapper;

import java.time.LocalDateTime;
import java.util.List;

import com.mrmrscart.helpandsupportservice.entity.EUserType;
import com.mrmrscart.helpandsupportservice.entity.HelpSupportMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HelpSupportQueryWrapper {
	private Long ticketId;
	private String issueType;
	private String orderId;
	private String issueSubject;
	private EUserType userFromType;
	private String userFromId;
	private EUserType userToType;
	private String userToId;
	private String ticketStatus;
	private String forwardedToId;
	private String forwardedToType;
	private boolean isDeleted;
	private String supplierId;
	private String createdBy;
	private LocalDateTime createdDate;
	private String lastModifiedBy;
	private LocalDateTime lastModifiedDate;
	private List<HelpSupportMessage> helpSupportMessages;
}
