package com.mrmrscart.userservice.entity.supplier;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.mrmrscart.userservice.audit.Audit;
import com.mrmrscart.userservice.util.ListToStringConverter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_supplier_store_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SupplierStoreInfo extends Audit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long supplierStoreInfoId;

	private String supplierStoreCode;

	private String supplierStoreName;

	private BigDecimal minimumOrderAmount;

	@Column(length = 50)
	private String maxOrderProcessingTime;

	private String maxOrderDeliveryRange;

	@Convert(converter = ListToStringConverter.class)
	private List<String> shopOpeningDays;

	@Column(length = 50)
	private String shopTimings;

	@Column(length = 255)
	private String shopDescription;

	@Column(length = 255)
	private String shopDescriptionImageUrl;

	private boolean isStoreActive;

	@Column(length = 255)
	private String supplierStoreLogo;

	private int activeProductCount;

	@ManyToMany(cascade = CascadeType.ALL)
	private List<SupplierStoreTheme> storeThemes;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "supplier_store_info_id")
	private List<SupplierStoreCoupon> supplierStoreCoupons;

	
}
