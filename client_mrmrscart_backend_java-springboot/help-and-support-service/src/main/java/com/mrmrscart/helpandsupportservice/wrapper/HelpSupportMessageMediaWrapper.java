package com.mrmrscart.helpandsupportservice.wrapper;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HelpSupportMessageMediaWrapper {

	private Long helpSupportMediaId;
	private String mediaUrl;
	private LocalDateTime createdAt;
}
