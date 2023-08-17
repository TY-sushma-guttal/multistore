//package com.mrmrscart.userservice.entity.supplier;
//
//import java.math.BigInteger;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import javax.persistence.CascadeType;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.ManyToMany;
//import javax.persistence.Table;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Table(name = "mmc_staff_capability")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class StaffCapability {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private BigInteger capabilityId;
//	
//	@Column(length = 50)
//	private String capabilityName;
//	
//	@Column(length = 45)
//	private String createdBy;
//	
//	@Column(length = 45)
//	private String lastUpdatedBy;
//	
//	@Column(length = 45)
//	private String capabilityType;
//	
//	private LocalDateTime createdAt;
//	
//	private LocalDateTime lastUpdatedAt;
//	
//	@ManyToMany(cascade = CascadeType.ALL)
//	private List<StaffManagementInfo> staffManagementInfos;
//}
