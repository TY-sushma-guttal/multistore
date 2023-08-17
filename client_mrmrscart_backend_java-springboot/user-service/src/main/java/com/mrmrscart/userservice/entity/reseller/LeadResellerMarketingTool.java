//package com.mrmrscart.userservice.entity.reseller;
//
//import javax.persistence.EmbeddedId;
//import javax.persistence.Entity;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.MapsId;
//import javax.persistence.Table;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Table(name = "mmc_lead_reseller_marketing_tool")
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class LeadResellerMarketingTool {
//	@EmbeddedId
//	private LeadResellerMarketingToolKey id;
//
//	@ManyToOne
//	@MapsId("marketing_tool_id")
//	@JoinColumn(name = "marketing_tool_id")
//	private ResellerMarketingTool resellerMarketingTool;
//
//	@ManyToOne
//	@MapsId("lead_id")
//	@JoinColumn(name = "lead_id")
//	private ResellerLead resellerLead;
//
//	private boolean isOld;
//	private boolean isRegistered;
//	private int usageCount;
//}
package com.mrmrscart.userservice.entity.reseller;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_lead_reseller_marketing_tool")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeadResellerMarketingTool {
	@EmbeddedId
	private LeadResellerMarketingToolKey id;

	@ManyToOne
	@MapsId("marketingToolId")
	@JoinColumn(name = "marketing_tool_id")
	private UserMarketingTool resellerMarketingTool;

	@ManyToOne
	@MapsId("leadId")
	@JoinColumn(name = "lead_id")
	private ResellerLead resellerLead;

	private boolean isOld;
	private boolean isRegistered;
	private int usageCount;
}
