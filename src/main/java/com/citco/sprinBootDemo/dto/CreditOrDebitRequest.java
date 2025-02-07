package com.citco.sprinBootDemo.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditOrDebitRequest {
	private String accountNumber;
	private BigDecimal amount;
}
