
package com.mrmrscart.userservice.entity.customer;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerMarketingToolsKey implements Serializable {

	private String customerId;
	private Long marketingToolId;
}
