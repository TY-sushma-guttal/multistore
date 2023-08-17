package com.mrmrscart.userservice.controller.reseller;

import static com.mrmrscart.userservice.common.reseller.MarketingToolThemeConstant.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mrmrscart.userservice.pojo.reseller.MarketingToolThemePojo;
import com.mrmrscart.userservice.response.admin.SuccessResponse;
import com.mrmrscart.userservice.service.reseller.MarketingToolThemeService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/users")
public class MarketingToolThemeController {

	@Autowired
	private MarketingToolThemeService marketingToolThemeService;

	@PostMapping("/marketing-tool-theme")
	public ResponseEntity<SuccessResponse> addMarketingToolTheme(@RequestBody MarketingToolThemePojo data) {
		return new ResponseEntity<>(new SuccessResponse(false, MARKETING_TOOL_ADD_SUCCESS_MESSAGE,
				marketingToolThemeService.addMarketingToolTheme(data)), HttpStatus.OK);
	}

	@GetMapping("/marketing-tool-theme/{id}")
	public ResponseEntity<SuccessResponse> getMarketingToolTheme(@PathVariable Long id) {
		return new ResponseEntity<>(new SuccessResponse(false, MARKETING_TOO_FETCH_SUCCESS_MESSAGE,
				marketingToolThemeService.getMarketingToolThemeById(id)), HttpStatus.OK);
	}

	@GetMapping("/marketing-tool-themes")
	public ResponseEntity<SuccessResponse> getAllMarketingToolTheme() {
		return new ResponseEntity<>(new SuccessResponse(false, MARKETING_TOO_FETCH_SUCCESS_MESSAGE,
				marketingToolThemeService.getAllMarketingToolTheme()), HttpStatus.OK);
	}

}
