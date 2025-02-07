package com.citco.sprinBootDemo.service.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Phaser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.citco.sprinBootDemo.dto.EmailDetails;
import com.citco.sprinBootDemo.entity.Transactions;
import com.citco.sprinBootDemo.entity.User;
import com.citco.sprinBootDemo.repository.TransactionRepository;
import com.citco.sprinBootDemo.repository.UserRepository;
import com.citco.sprinBootDemo.service.EmailService;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BankStatement {

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmailService emailService;

	private static final String file = "C:\\ZonePranav\\personalProject\\MyStatement.pdf";

	public List<Transactions> generateStatement(String accountNumber, String startDate, String endDate)
			throws FileNotFoundException, DocumentException {
		LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
		LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
		List<Transactions> transactionsList = transactionRepository.findAll().stream()
				.filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
				.filter(transaction -> transaction.getCreatedAt().isEqual(start))
				.filter(transaction -> transaction.getCreatedAt().isEqual(end)).toList();

		User user = userRepository.findByAccountNumber(accountNumber);
		String customerName = user.getFirstName() + " " + user.getLastName();
		String customerAddress = user.getAddress();
		Rectangle statementSize = new Rectangle(PageSize.A4);
		Document document = new Document(statementSize);
		log.info("Setting size of Document");

		OutputStream outputStream = new FileOutputStream(file);
		PdfWriter.getInstance(document, outputStream);
		document.open();

		PdfPTable bankInfoTable = new PdfPTable(1);
		PdfPCell bankName = new PdfPCell(new Phrase("Chit Fund Bank"));
		bankName.setBorder(0);
		bankName.setBackgroundColor(BaseColor.BLUE);
		bankName.setPadding(20f);

		PdfPCell bankAddress = new PdfPCell(new Phrase("railway gate,Vaiduwadi,Hadapsar, Pune, Maharashtra 411013"));
		bankAddress.setBorder(0);
		bankInfoTable.addCell(bankName);
		bankInfoTable.addCell(bankAddress);

		PdfPTable statementInfo = new PdfPTable(2);
		PdfPCell customerInfo = new PdfPCell(new Phrase("Start Date : " + startDate));
		customerInfo.setBorder(0);

		PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
		statement.setBorder(0);

		PdfPCell stopDate = new PdfPCell(new Phrase("End Date : " + endDate));
		stopDate.setBorder(0);

		PdfPCell name = new PdfPCell(new Phrase("Customer Name:  " + customerName));
		name.setBorder(0);

		PdfPCell space = new PdfPCell();
		PdfPCell address = new PdfPCell(new Phrase("Customer Address:  " + customerAddress));

		PdfPTable transactionsTable = new PdfPTable(4);
		PdfPCell date = new PdfPCell(new Phrase("Date"));
		date.setBackgroundColor(BaseColor.BLUE);
		date.setBorder(0);

		PdfPCell transactionType = new PdfPCell(new Phrase("TRANSACTION TYPE"));
		transactionType.setBackgroundColor(BaseColor.BLUE);
		transactionType.setBorder(0);

		PdfPCell transactionAmount = new PdfPCell(new Phrase("TRANSACTION AMOUNT"));
		transactionAmount.setBackgroundColor(BaseColor.BLUE);
		transactionAmount.setBorder(0);

		PdfPCell status = new PdfPCell(new Phrase("STATUS"));
		status.setBackgroundColor(BaseColor.BLUE);
		status.setBorder(0);

		transactionsTable.addCell(date);
		transactionsTable.addCell(transactionType);
		transactionsTable.addCell(transactionAmount);
		transactionsTable.addCell(status);

		transactionsList.forEach(transaction -> {
			transactionsTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
			transactionsTable.addCell(new Phrase(transaction.getTransactionsType()));
			transactionsTable.addCell(new Phrase(transaction.getAmount().toString()));
			transactionsTable.addCell(new Phrase(transaction.getStatus()));
		});

		statementInfo.addCell(customerInfo);
		statementInfo.addCell(statement);
		statementInfo.addCell(stopDate);
		statementInfo.addCell(name);
		statementInfo.addCell(space);
		statementInfo.addCell(name);
		statementInfo.addCell(address);

		document.add(bankInfoTable);
		document.add(statementInfo);
		document.add(transactionsTable);

		document.close();
		EmailDetails emailDetails = EmailDetails.builder().recipient(user.getEmail()).subject("STATEMENT OF ACCOUNT")
				.messageBody("kindly find your requested account statement attached!").attachment(file).build();

		emailService.sendEmailWithAttachment(emailDetails);

		return transactionsList;
	}

	private void designStatement(List<Transactions> transactionsList) throws FileNotFoundException, DocumentException {

	}

}
