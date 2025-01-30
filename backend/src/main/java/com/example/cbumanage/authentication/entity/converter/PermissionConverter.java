package com.example.cbumanage.authentication.entity.converter;

import com.example.cbumanage.authentication.authorization.Permission;
import jakarta.persistence.AttributeConverter;
import org.json.JSONArray;

import java.util.List;

public class PermissionConverter implements AttributeConverter<List<Permission>, String> {
	@Override
	public String convertToDatabaseColumn(List<Permission> attribute) {
		JSONArray jsonArray = new JSONArray(attribute);
		return jsonArray.toString();
	}

	@Override
	public List<Permission> convertToEntityAttribute(String dbData) {
		JSONArray jsonArray = new JSONArray(dbData);
		return jsonArray.toList().stream().map(v -> Permission.getValue(v.toString())).toList();
	}
}
