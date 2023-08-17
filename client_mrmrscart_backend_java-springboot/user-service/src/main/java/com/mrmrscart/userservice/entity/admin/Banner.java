package com.mrmrscart.userservice.entity.admin;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mrmrscart.userservice.audit.Audit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_banners")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class Banner extends Audit{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bannerId;
	
	@Column(length = 255)
	private String bannerImageUrlForWeb;
	
	@Column(length = 255)
	private String bannerImageUrlForMobile;
	
	@Column(length = 50)
	private String panelName;
	
	@Column(length = 255)
	private String navigationUrl;
	
	@Column(length = 35)
	private String displayPage;
	
	@Column(length = 20)
	private String buttonName;
	
	@Column(length = 20)
	private EBannerStatus status;
	
	private boolean isDisable;
	
	@Column
	private boolean isDelete;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime startDateTime;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime endDateTime;
}
