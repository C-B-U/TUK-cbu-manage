package com.example.cbumanage.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class MemberCreateDTO {
	@NotNull
	@NotEmpty
	private String name;
	@NotNull
	@NotEmpty
	private String phoneNumber;
	@NotNull
	@NotEmpty
	private String major;
	@NotNull
	@NotEmpty
	private String grade;
	@NotNull
	@NotEmpty
	private Long studentNumber;
	@NotNull
	@NotEmpty
	private Long generation;
	@NotNull
	private String note;
}
