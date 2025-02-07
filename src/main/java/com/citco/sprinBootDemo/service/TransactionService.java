package com.citco.sprinBootDemo.service;

import org.springframework.stereotype.Service;

import com.citco.sprinBootDemo.dto.TransactionDto;

@Service
public interface TransactionService {
	void saveTransaction(TransactionDto transactions);

}
