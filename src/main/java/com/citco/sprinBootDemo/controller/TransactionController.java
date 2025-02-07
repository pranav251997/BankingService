package com.citco.sprinBootDemo.controller;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.citco.sprinBootDemo.entity.Transactions;
import com.citco.sprinBootDemo.service.impl.BankStatement;
import com.itextpdf.text.DocumentException;

@RestController
@RequestMapping("/bankStatment")
public class TransactionController {

	@Autowired
	private BankStatement bankStatement;

	@GetMapping
	public List<Transactions> generateBankStatement(@RequestParam String accountNumber, @RequestParam String startate,
			@RequestParam String endDate) throws FileNotFoundException, DocumentException {
		return bankStatement.generateStatement(accountNumber, startate, endDate);

	}
}
