package com.mrmrscart.orderspaymentsservice.feign.pojo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
	private Long id;
	private String profileId;
	private String profileName;
	private String profileImageUrl;
	private boolean isProfilePrimary;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
