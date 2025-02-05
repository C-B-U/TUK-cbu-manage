package com.example.cbumanage.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailAndPasswordDTO {
	private String email;
	private String password;
}
