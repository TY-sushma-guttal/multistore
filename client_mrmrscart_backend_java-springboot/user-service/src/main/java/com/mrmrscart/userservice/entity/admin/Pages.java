package com.mrmrscart.userservice.entity.admin;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_pages")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pages {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger pageId;
	@Column(length = 50)
	private String pageName;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Banner> banners;

}
