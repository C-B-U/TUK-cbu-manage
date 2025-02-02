package com.example.cbumanage.authentication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequestDTO {
	@Email
	@NotNull @NotEmpty
	private String email;

	@Size(min = 8)
	@NotNull @NotEmpty
	private String password;

	@NotNull @NotEmpty
	@Size(min = 2)
	private String name;

	@NotNull
	private Long studentNumber;

	@NotNull @NotEmpty
	@Size(min = 3)
	private String nickname;
}
