package com.citco.sprinBootDemo.dto;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
	private String firstName;
	private String lastName;
	private String otherName;
	private String gender;
	private String address;
	private String stateOfOrigin;
	private String email;
	private String password;
	private String phoneNumber; // Validation
	private String alternativePhoneNumber;
	private LocalDateTime createDate;
}
