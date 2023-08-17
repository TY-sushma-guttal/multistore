package com.mrmrscart.userservice.pojo.admin;

import java.util.List;

import com.mrmrscart.userservice.entity.admin.EAdminStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminManagerFilterPojo {
	private List<EAdminStatus> adminStatusList;
	private List<String> createdByList;
	private String keyWord;
	private int pageNumber;
	private int pageSize;
}
