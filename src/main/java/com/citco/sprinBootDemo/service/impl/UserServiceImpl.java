package com.citco.sprinBootDemo.service.impl;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.citco.sprinBootDemo.config.JwtTokenProvider;
import com.citco.sprinBootDemo.dto.AccountInfo;
import com.citco.sprinBootDemo.dto.BankResponse;
import com.citco.sprinBootDemo.dto.CreditOrDebitRequest;
import com.citco.sprinBootDemo.dto.EmailDetails;
import com.citco.sprinBootDemo.dto.EnquiryRequest;
import com.citco.sprinBootDemo.dto.TransactionDto;
import com.citco.sprinBootDemo.dto.TransferRequest;
import com.citco.sprinBootDemo.dto.UserRequest;
import com.citco.sprinBootDemo.dto.loginDto;
import com.citco.sprinBootDemo.entity.Role;
import com.citco.sprinBootDemo.entity.User;
import com.citco.sprinBootDemo.repository.UserRepository;
import com.citco.sprinBootDemo.service.EmailService;
import com.citco.sprinBootDemo.service.TransactionService;
import com.citco.sprinBootDemo.service.UserService;
import com.citco.sprinBootDemo.utils.AccountUtils;

import jakarta.validation.constraints.Email;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtTokenProvider jwtTokenProvider;

	@Override
	@Transactional
	public BankResponse createAccount(UserRequest userRequest) {
		Optional<User> ifExist = userRepository.findByEmail(userRequest.getEmail());
		if (ifExist.isPresent()) {
			return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
					.responseMessage(AccountUtils.ACCOUNT_EXISTS_CODE).accountInfo(null).build();

		}

		User newUser = User.builder().firstName(userRequest.getFirstName()).lastName(userRequest.getLastName())
				.otherName(userRequest.getOtherName()).gender(userRequest.getGender()).address(userRequest.getAddress())
				.stateOfOrigin(userRequest.getStateOfOrigin()).accountNumber(AccountUtils.generateAccountNumber())
				.accountBalance(BigDecimal.ZERO).email(userRequest.getEmail())
				.password(passwordEncoder.encode(userRequest.getPassword())).role(Role.valueOf("ROLE_ADMIN"))
				.phoneNumber(userRequest.getPhoneNumber())
				.alternativePhoneNumber(userRequest.getAlternativePhoneNumber()).status("ACTIVE").build();

		User savedUser = saveUser(newUser);
		// Email Notification
		EmailDetails emailDetails = EmailDetails.builder().recipient(savedUser.getEmail()).subject("Account Creation")
				.messageBody("Congratulation your account Successfully Created.\nYour Account Details : \n"
						+ "Account Name: " + savedUser.getFirstName() + "\n Account Number: "
						+ savedUser.getAccountNumber())
				.build();
		emailService.sendEmailAlert(emailDetails);

		return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
				.responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
				.accountInfo(AccountInfo.builder().accountBalence(savedUser.getAccountBalance())
						.accountName(savedUser.getFirstName() + " " + savedUser.getLastName())
						.accountNumber(savedUser.getAccountNumber()).build())

				.build();
	}

	private User saveUser(User user) {
		return userRepository.save(user);
	}

	private boolean isAccountExists(String accountNumber) {
		return userRepository.existsByAccountNumber(accountNumber);
	}

	private User foundUser(String accountNumber) {
		return userRepository.findByAccountNumber(accountNumber);
	}

	private void saveTransaction(TransactionDto transactionDto) {
		transactionService.saveTransaction(transactionDto);
	}

	@Override
	public BankResponse login(loginDto loginDto) {
		Authentication authentication = null;
		authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

		EmailDetails loginAlert = EmailDetails.builder().subject("Your logged in !").recipient(loginDto.getEmail())
				.messageBody(
						"You logged into your account If you did not initiate this request, Please contact your bank")
				.build();

		emailService.sendEmailAlert(loginAlert);

		return BankResponse.builder().responseCode("Login Success")
				.responseMessage(jwtTokenProvider.generateToken(authentication)).build();

	}

	@Override
	public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
		// TODO Auto-generated method stub
		boolean isAccountExists = isAccountExists(enquiryRequest.getAccountNumber());
		if (!isAccountExists) {
			return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE).accountInfo(null).build();
		}
		User foundUser = foundUser(enquiryRequest.getAccountNumber());
		// Email Notification (All Details Information )
		return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
				.responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
				.accountInfo(AccountInfo.builder().accountName(foundUser.getFirstName() + " " + foundUser.getLastName())
						.accountBalence(foundUser.getAccountBalance()).accountNumber(foundUser.getAccountNumber())
						.build())
				.build();
	}

	@Override
	public String nameEnquiry(EnquiryRequest enquiryRequest) {
		boolean isAccountExists = isAccountExists(enquiryRequest.getAccountNumber());
		if (!isAccountExists) {
			return AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE;
		}
		User foundUser = foundUser(enquiryRequest.getAccountNumber());
		// Email Notification (All Details Information )
		return foundUser.getFirstName() + " " + foundUser.getLastName();
	}

	@Override
	public BankResponse creditAccount(CreditOrDebitRequest creditOrDebitRequest) {
		boolean isAccountExists = isAccountExists(creditOrDebitRequest.getAccountNumber());
		if (!isAccountExists) {
			return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE).accountInfo(null).build();
		}
		User userToCreadit = foundUser(creditOrDebitRequest.getAccountNumber());
		userToCreadit.setAccountBalance(userToCreadit.getAccountBalance().add(creditOrDebitRequest.getAmount()));
		User updateUser = saveUser(userToCreadit);
		// Email Notification (All Details Information )

		TransactionDto transactionDto = TransactionDto.builder().accountNumber(userToCreadit.getAccountNumber())
				.transactionsType("CREDIT").amount(creditOrDebitRequest.getAmount()).build();

		saveTransaction(transactionDto);

		return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_CREDIT_CODE)
				.responseMessage(AccountUtils.ACCOUNT_CREDIT_SUCCESS)
				.accountInfo(
						AccountInfo.builder().accountName(updateUser.getFirstName() + " " + updateUser.getLastName())
								.accountBalence(updateUser.getAccountBalance())
								.accountNumber(updateUser.getAccountNumber()).build())
				.build();
	}

	@Override
	public BankResponse debitAccount(CreditOrDebitRequest creditOrDebitRequest) {
		boolean isAccountExists = isAccountExists(creditOrDebitRequest.getAccountNumber());
		if (!isAccountExists) {
			return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE).accountInfo(null).build();
		}
		User userToDebit = foundUser(creditOrDebitRequest.getAccountNumber());
		userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(creditOrDebitRequest.getAmount()));
		User updateUser = saveUser(userToDebit);
		// Email Notification (All Details Information )

		TransactionDto transactionDto = TransactionDto.builder().accountNumber(userToDebit.getAccountNumber())
				.transactionsType("DEBIT").amount(creditOrDebitRequest.getAmount()).build();

		saveTransaction(transactionDto);

		return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_DEBIT_CODE)
				.responseMessage(AccountUtils.ACCOUNT_DEBIT_SUCCESS)
				.accountInfo(
						AccountInfo.builder().accountName(updateUser.getFirstName() + " " + updateUser.getLastName())
								.accountBalence(updateUser.getAccountBalance())
								.accountNumber(updateUser.getAccountNumber()).build())
				.build();

	}

	@Override
	public BankResponse transfer(TransferRequest request) {
		boolean source = isAccountExists(request.getSourceAccountNumber());
		boolean destination = isAccountExists(request.getDestinationAccountNumber());
		if (!source || !destination) {
			return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
					.responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE).accountInfo(null).build();
		}
		User sourceAcc = foundUser(request.getSourceAccountNumber());
		if (request.getAmount().compareTo(sourceAcc.getAccountBalance()) == 1) {
			return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_HAVE_INSUFFICIENT_FUND_CODE)
					.responseMessage(AccountUtils.ACCOUNT_HAVE_INSUFFICIENT_FUND).accountInfo(null).build();
		}
		sourceAcc.setAccountBalance(sourceAcc.getAccountBalance().subtract(request.getAmount()));
		saveUser(sourceAcc);
		// Email Notification (All Details Information )
		User destinationAcc = foundUser(request.getDestinationAccountNumber());
		destinationAcc.setAccountBalance(destinationAcc.getAccountBalance().add(request.getAmount()));
		saveUser(destinationAcc);
		// Email Notification (All Details Information )

		TransactionDto transactionDto = TransactionDto.builder().accountNumber(destinationAcc.getAccountNumber())
				.transactionsType("CREDIT").amount(request.getAmount()).build();

		saveTransaction(transactionDto);

		return BankResponse.builder().responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
				.responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE).accountInfo(null).build();
	}

}
