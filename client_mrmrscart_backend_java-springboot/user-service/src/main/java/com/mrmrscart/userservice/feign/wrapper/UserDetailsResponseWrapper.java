package com.mrmrscart.userservice.feign.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsResponseWrapper {
	private String userId;
	private String userName;

}
