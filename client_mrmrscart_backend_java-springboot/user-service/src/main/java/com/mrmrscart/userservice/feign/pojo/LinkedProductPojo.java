package com.mrmrscart.userservice.feign.pojo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkedProductPojo {
	private List<String> upSells;
	private List<String> crossSells;
}
