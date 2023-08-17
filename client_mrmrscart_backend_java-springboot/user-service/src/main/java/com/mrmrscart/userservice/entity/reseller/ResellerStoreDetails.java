package com.mrmrscart.userservice.entity.reseller;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.mrmrscart.userservice.entity.admin.ArticlesMedia;
import com.mrmrscart.userservice.entity.customer.CustomerRegistration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_reseller_store_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResellerStoreDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long resellerStoreId;
	@Column(length = 35)
	private String resellerStoreName;
	@Column(length = 50)
	private String resellerStoreCode;
	private double productGlobalMargin;
	@Column(length = 255)
	private String themeDetails;
	@Column(length = 35)
	private String storeType;
	@Column(length = 20)
	private String storeOwnerType;
	@Column(length = 45)
	private String storeOwnerId;
	@OneToOne(cascade = CascadeType.ALL)
	private ArticlesMedia media;
	@ManyToMany(cascade = CascadeType.ALL)
	private List<CustomerRegistration> customerRegistrations;
	

}
