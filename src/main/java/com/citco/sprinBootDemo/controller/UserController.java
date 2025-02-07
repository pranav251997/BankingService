package com.citco.sprinBootDemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.citco.sprinBootDemo.dto.BankResponse;
import com.citco.sprinBootDemo.dto.CreditOrDebitRequest;
import com.citco.sprinBootDemo.dto.EnquiryRequest;
import com.citco.sprinBootDemo.dto.TransferRequest;
import com.citco.sprinBootDemo.dto.UserRequest;
import com.citco.sprinBootDemo.dto.loginDto;
import com.citco.sprinBootDemo.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/user")
@Tag(name = "User Account Management API")
public class UserController {

	@Autowired
	private UserService userService;

	@Operation(summary = "Create New User Account", description = "Creating a new user and assigning an account ID")
	@ApiResponse(responseCode = "201", description = "Http Status 201 CREATED")
	@PostMapping("/createAccount")
	public BankResponse createdAccount(@RequestBody UserRequest userRequest) {
		return userService.createAccount(userRequest);
	}

	@GetMapping("/balanceEnquiry")
	public BankResponse balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
		return userService.balanceEnquiry(enquiryRequest);

	}

	@GetMapping("/nameEnquiry")
	public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
		return userService.nameEnquiry(enquiryRequest);
	}

	@PostMapping("/creditAmount")
	public BankResponse creditAmount(@RequestBody CreditOrDebitRequest creditOrDebitRequest) {
		return userService.creditAccount(creditOrDebitRequest);
	}

	@PostMapping("/debitAmount")
	public BankResponse debitAccount(@RequestBody CreditOrDebitRequest creditOrDebitRequest) {
		return userService.debitAccount(creditOrDebitRequest);
	}

	@PostMapping("/transfer")
	public BankResponse transfer(@RequestBody TransferRequest transferRequest) {
		return userService.transfer(transferRequest);
	}

	@PostMapping("/login")
	public BankResponse login(@RequestBody loginDto loginDto) {
		return userService.login(loginDto);
	}

}
