package com.mrmrscart.authservice.entity.user;

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
@Table(name = "mmc_user_login")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLogin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userLoginId;
	@Column(length = 255)
	private String emailId;
	@Column(length = 255)
	private String password;
	@Column(length = 255)
	private String refreshToken;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_login_id")
	private List<UserRole> userRoles;
}
