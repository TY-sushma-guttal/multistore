package com.mrmrscart.userservice.entity.customer;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_wishlist_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishlistDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger wishlistId;
	@Column(length = 20)
	private String wishlistName;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "wishlist_id")
	private List<WishlistProducts> wishlistProducts;
	
}
