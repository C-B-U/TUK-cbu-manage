package com.example.cbumanage.authentication.dto;

import lombok.Data;

@Data
public class SignUpRequestDTO {
	private String email;
	private String password;
	private String name;
	private Long studentNumber;
	private String nickname;
}
