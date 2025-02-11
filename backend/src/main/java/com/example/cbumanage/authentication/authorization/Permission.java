package com.example.cbumanage.authentication.authorization;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum Permission {
	MEMBER(Set.of("/api/v1/*"), Set.of("/api/v1/login")),
//	MEMBER(Set.of(), Set.of()),
	ADMIN(Set.of(), Set.of())
	;

	@Getter
	private Set<String> path;
	@Getter
	private Set<String> exclusivePath;
	@Getter
	private String name;

	Permission(Set<String> path, Set<String> exclusivePath, String name) {
		this.path = path;
		this.exclusivePath = exclusivePath;
		this.name = name;
	}

	Permission(Set<String> path, Set<String> exclusivePath) {
		this.path = path;
		this.exclusivePath = exclusivePath;
		this.name = this.name().toLowerCase();
	}

	@Override
	public String toString() {
		return this.name;
	}

	private static Map<String, Permission> permissionMap = init();
	private static Map<String, Permission> init() {
		HashMap<String, Permission> result = new HashMap<>();
		for (Permission value : Permission.values()) {
			result.put(value.name, value);
		}
		return result;
	}
	public static Permission getValue(String name) {
		return permissionMap.get(name);
	}
}
