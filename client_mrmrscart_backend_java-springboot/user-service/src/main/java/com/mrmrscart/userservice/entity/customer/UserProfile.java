package com.mrmrscart.userservice.entity.customer;

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

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_user_profile")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String profileId;
	@Column(length = 50)
	private String profileName;
	@Column(length = 255)
	private String profileImageUrl;
	private boolean isProfilePrimary;
	@CreatedDate
	private LocalDateTime createdAt;
	@LastModifiedDate
	private LocalDateTime updatedAt;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "id")
	private List<WishlistDetails> wishlistDetails;
}
