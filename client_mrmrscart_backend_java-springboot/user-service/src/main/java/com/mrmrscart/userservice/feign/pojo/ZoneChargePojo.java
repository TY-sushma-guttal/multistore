package com.mrmrscart.userservice.feign.pojo;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZoneChargePojo {
	private BigDecimal zoneAcharge;
	private BigDecimal zoneBcharge;
	private BigDecimal zoneCcharge;
	private BigDecimal zoneDcharge;
	private BigDecimal zoneEcharge;
}
