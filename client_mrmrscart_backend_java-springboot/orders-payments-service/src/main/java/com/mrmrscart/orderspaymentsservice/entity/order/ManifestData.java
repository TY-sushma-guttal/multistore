package com.mrmrscart.orderspaymentsservice.entity.order;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_manifest_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManifestData {

	@Id
	@GeneratedValue
	private Long manifestId;
	
	private LocalDateTime manifestDate;
	
	@Column(length = 255)
	private String manifestFielUrl;
	
	@Column(length = 50)
	private String manifestFileName;
	
	private LocalDateTime lastUpdatedAt;
}
