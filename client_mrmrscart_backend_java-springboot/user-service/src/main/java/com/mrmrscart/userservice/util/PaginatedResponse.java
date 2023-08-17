package com.mrmrscart.userservice.util;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaginatedResponse {

	public static List<?> getPaginatedResponse(List<?> list, int pageNumber, int pageSize) {
		if (pageNumber == 0) {
			return list.stream().limit(pageSize).collect(Collectors.toList());
		} else {
			int skipCount = (pageNumber) * pageSize;
			return list.stream().skip(skipCount).limit(pageSize).collect(Collectors.toList());
		}
	}
}
