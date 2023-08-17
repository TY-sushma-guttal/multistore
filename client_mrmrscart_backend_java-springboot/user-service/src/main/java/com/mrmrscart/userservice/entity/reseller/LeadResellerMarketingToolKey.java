//package com.mrmrscart.userservice.entity.reseller;
//
//import java.math.BigInteger;
//
//import javax.persistence.Embeddable;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Embeddable
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class LeadResellerMarketingToolKey {
//	
//	private BigInteger leadId;
//	private BigInteger marketingToolId;
//}

package com.mrmrscart.userservice.entity.reseller;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeadResellerMarketingToolKey implements Serializable {
	
	private Long leadId;
	private Long marketingToolId;
}
