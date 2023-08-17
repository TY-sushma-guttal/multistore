package com.mrmrscart.helpandsupportservice.wrapper;

import java.time.LocalDateTime;

import com.mrmrscart.helpandsupportservice.entity.EUserType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HelpSupportMessageViewWrapper {

	private Long viewId;
	private EUserType viewedByType;
	private String viewedById;
	private LocalDateTime viewedDateTime;
	private LocalDateTime updatedAt;
}
