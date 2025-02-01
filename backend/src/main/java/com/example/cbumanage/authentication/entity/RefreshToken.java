package com.example.cbumanage.authentication.entity;

import com.example.cbumanage.utils.UUIDProvider;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
	@Id
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;
	private Long userId;
	private Long exp;

	public RefreshToken(Long userId, Long exp) {
		this.id = UUIDProvider.random();
		this.userId = userId;
		this.exp = exp;
	}
}
