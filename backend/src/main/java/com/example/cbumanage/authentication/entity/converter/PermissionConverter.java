package com.example.cbumanage.authentication.entity.converter;

import com.example.cbumanage.authentication.authorization.Permission;
import jakarta.persistence.AttributeConverter;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class PermissionConverter implements AttributeConverter<List<Permission>, String> {
	@Override
	public String convertToDatabaseColumn(List<Permission> attribute) {
		JSONArray jsonArray = new JSONArray();
		for (Permission permission : attribute) {
			jsonArray.put(permission.getName());
		}
		return jsonArray.toString();
	}

	@Override
	public List<Permission> convertToEntityAttribute(String dbData) {
		if (dbData == null) return List.of();
		List<Permission> list = new ArrayList<>();
		JSONArray jsonArray = new JSONArray(dbData);
		for (int i = 0; i < jsonArray.length(); i++) {
			list.add(Permission.getValue(jsonArray.getString(i)));
		}
		return list;
//		return jsonArray.toList().stream().map(v -> Permission.getValue(v.toString())).toList();
	}
}
