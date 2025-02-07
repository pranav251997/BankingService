package com.citco.sprinBootDemo.utils;

import java.time.Year;

public class AccountUtils {

	public static final String ACCOUNT_EXISTS_CODE = "001";
	public static final String ACCOUNT_EXISTS_MESSAGE = "This user already has an account Created!";
	public static final String ACCOUNT_CREATION_SUCCESS = "002";
	public static final String ACCOUNT_CREATION_MESSAGE = "Account has been Successfully created!";
	public static final String ACCOUNT_NOT_EXISTS_CODE = "003";
	public static final String ACCOUNT_NOT_EXISTS_MESSAGE = "User with the provided Account Number does not exist";
	public static final String ACCOUNT_FOUND_CODE = "004";
	public static final String ACCOUNT_FOUND_MESSAGE = "User Account Found!";
	public static final String ACCOUNT_CREDIT_CODE = "005";
	public static final String ACCOUNT_CREDIT_SUCCESS = "Account has been Successfully Credited!";

	public static final String ACCOUNT_DEBIT_CODE = "005";
	public static final String ACCOUNT_DEBIT_SUCCESS = "Account has been Successfully Credited!";

	public static final String ACCOUNT_HAVE_INSUFFICIENT_FUND_CODE = "006";
	public static final String ACCOUNT_HAVE_INSUFFICIENT_FUND = "Account have Insufficient Fund";

	public static final String TRANSFER_SUCCESSFUL_CODE = "007";
	public static final String TRANSFER_SUCCESSFUL_MESSAGE = "Transfer Successful!";

	public static String generateAccountNumber() {
		Year currentYear = Year.now();
		int min = 100000;
		int max = 999999;
		int randNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);

		String year = String.valueOf(currentYear);
		String randomNumber = String.valueOf(randNumber);
		StringBuilder accountNumber = new StringBuilder();
		accountNumber.append(year).append(randomNumber);
		return accountNumber.toString();
	}
}
