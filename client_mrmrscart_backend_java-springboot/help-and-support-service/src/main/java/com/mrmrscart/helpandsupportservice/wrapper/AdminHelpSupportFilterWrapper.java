package com.mrmrscart.helpandsupportservice.wrapper;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminHelpSupportFilterWrapper {

	private String filterName;
	private List<String> filterValue;
}
