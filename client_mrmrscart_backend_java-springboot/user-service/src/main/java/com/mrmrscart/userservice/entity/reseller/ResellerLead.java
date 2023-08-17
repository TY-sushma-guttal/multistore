package com.mrmrscart.userservice.entity.reseller;

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
@Table(name = "mmc_reseller_lead")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResellerLead {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long leadId;
	@Column(length = 50)
	private String leadName;
	@Column(length = 13)
	private String mobileNumber;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "lead_id")
	private List<LeadResellerMarketingTool> leadResellerMarketingTools;
}
