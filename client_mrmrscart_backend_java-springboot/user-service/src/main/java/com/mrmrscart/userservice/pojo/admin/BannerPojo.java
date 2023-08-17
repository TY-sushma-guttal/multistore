package com.mrmrscart.userservice.pojo.admin;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mrmrscart.userservice.entity.admin.EBannerStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BannerPojo {
	
	private Long bannerId;
	private String bannerImageUrlForWeb;
	private String bannerImageUrlForMobile;
	private String panelName;
	private String displayPage;
	private String navigationUrl;
	private String buttonName;
	private EBannerStatus status;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime startDateTime;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime endDateTime;
}
