package com.citco.sprinBootDemo.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
	private String transactionsId;
	private String transactionsType;
	private BigDecimal amount;
	private String accountNumber;
	private String status;

}
