package com.mrmrscart.userservice.entity.supplier;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_user_address_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long addressId;
	@Column(length = 50)
	private String name;
	@Column(length = 13)
	private String mobileNumber;
	@Column(length = 6)
	private String pinCode;
	@Column(length = 255)
	private String location;
	@Column(length = 255)
	private String address;
	@Column(length = 50)
	private String cityDistrictTown;
	@Column(length = 50)
	private String state;
	@Column(length = 65)
	private String landmark;
	@Column(length = 100)
	private String latitudeValue;
	@Column(length = 100)
	private String longitudeValue;
	@Column(length = 13)
	private String alternativeMobileNumber;
	private boolean isPrimary;
	@CreatedDate
	private LocalDateTime createdAt;
	@LastModifiedDate
	private LocalDateTime updatedAt;
	private boolean isDeleted;
	private String supplierId;

}
