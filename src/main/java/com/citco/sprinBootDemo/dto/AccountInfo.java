package com.citco.sprinBootDemo.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfo {
	@Schema(name = "User Account Name")
	private String accountName;
	@Schema(name = "User Account Balence")
	private BigDecimal accountBalence;
	@Schema(name = "User Account Number")
	private String accountNumber;
}
