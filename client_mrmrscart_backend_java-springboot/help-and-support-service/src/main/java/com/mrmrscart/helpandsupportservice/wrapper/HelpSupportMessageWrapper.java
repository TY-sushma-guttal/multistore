package com.mrmrscart.helpandsupportservice.wrapper;

import java.time.LocalDateTime;
import java.util.List;

import com.mrmrscart.helpandsupportservice.entity.EUserType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HelpSupportMessageWrapper {

	private Long messageId;
	private String messageFromId;
	private EUserType messageFromType;
	private String message;
	private LocalDateTime messagedAt;
	private LocalDateTime updatedAt;
	private List<HelpSupportMessageMediaWrapper> helpSupportMessageMedias;
	private List<HelpSupportMessageViewWrapper> helpSupportMessageViews;
}

