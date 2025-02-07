package com.citco.sprinBootDemo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.citco.sprinBootDemo.dto.TransactionDto;
import com.citco.sprinBootDemo.dto.TransferRequest;
import com.citco.sprinBootDemo.entity.Transactions;
import com.citco.sprinBootDemo.repository.TransactionRepository;
import com.citco.sprinBootDemo.service.TransactionService;

@Service
public class TransactionImpl implements TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;

	@Override
	public void saveTransaction(TransactionDto transactionsDto) {
		Transactions transactions = Transactions.builder().transactionsType(transactionsDto.getTransactionsType())
				.accountNumber(transactionsDto.getAccountNumber()).amount(transactionsDto.getAmount()).status("SUCCESS")
				.build();

		transactionRepository.save(transactions);
		System.out.println("Transaction saved Successfully");
	}

}
