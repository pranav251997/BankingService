package com.citco.sprinBootDemo.service;

import org.springframework.stereotype.Service;

import com.citco.sprinBootDemo.dto.BankResponse;
import com.citco.sprinBootDemo.dto.CreditOrDebitRequest;
import com.citco.sprinBootDemo.dto.EnquiryRequest;
import com.citco.sprinBootDemo.dto.TransferRequest;
import com.citco.sprinBootDemo.dto.UserRequest;
import com.citco.sprinBootDemo.dto.loginDto;

@Service
public interface UserService {
	BankResponse createAccount(UserRequest userRequest);

	BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);

	String nameEnquiry(EnquiryRequest enquiryRequest);

	BankResponse creditAccount(CreditOrDebitRequest creditOrDebitRequest);

	BankResponse debitAccount(CreditOrDebitRequest creditOrDebitRequest);

	BankResponse transfer(TransferRequest request);

	BankResponse login(loginDto loginDto);
}
