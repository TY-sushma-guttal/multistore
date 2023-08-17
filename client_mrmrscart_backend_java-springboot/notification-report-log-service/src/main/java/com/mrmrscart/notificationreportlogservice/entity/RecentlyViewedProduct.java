package com.mrmrscart.notificationreportlogservice.entity;

import java.math.BigInteger;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mmc_recently_viewed_products")
public class RecentlyViewedProduct {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger recentlyViewedProductId;
	@Column(length = 45)
	private String productId;
	@Column(length = 45)
	private String variationId;
	@Column(length = 45)
	private String storeProductId;
	private LocalDateTime viewedAt;
	@Column(length = 45)
	private String viewedById;
}
