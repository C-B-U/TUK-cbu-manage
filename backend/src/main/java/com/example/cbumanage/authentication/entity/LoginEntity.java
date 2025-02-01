package com.example.cbumanage.authentication.entity;

import com.example.cbumanage.authentication.authorization.Permission;
import com.example.cbumanage.authentication.entity.converter.PermissionConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class LoginEntity {
	@Id
	private Long userId;
	@Column(unique = true, nullable = false)
	private String email;
	@Column(nullable = false)
	private String password;
	@Column(name = "permissions")
	@Convert(converter = PermissionConverter.class)
	private List<Permission> permissions;

	public LoginEntity(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
