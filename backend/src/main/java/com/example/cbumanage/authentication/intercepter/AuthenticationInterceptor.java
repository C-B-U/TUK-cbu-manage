package com.example.cbumanage.authentication.intercepter;

import com.example.cbumanage.authentication.authorization.Permission;
import com.example.cbumanage.authentication.dto.AccessAndRefreshTokenObjectDTO;
import com.example.cbumanage.authentication.dto.AccessToken;
import com.example.cbumanage.authentication.entity.RefreshToken;
import com.example.cbumanage.authentication.repository.RefreshTokenRepository;
import com.example.cbumanage.authentication.service.LoginService;
import com.example.cbumanage.model.enums.Role;
import com.example.cbumanage.utils.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.UUID;

public class AuthenticationInterceptor implements HandlerInterceptor {

	private final Permission permission;
	private final LoginService loginService;
	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtProvider jwtProvider;

	public AuthenticationInterceptor(Permission permission, LoginService loginService, RefreshTokenRepository refreshTokenRepository, JwtProvider jwtProvider) {
		this.permission = permission;
		this.loginService = loginService;
		this.refreshTokenRepository = refreshTokenRepository;
		this.jwtProvider = jwtProvider;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		AccessToken accessToken = ((AccessToken) request.getAttribute("ACCESS_TOKEN"));
		RefreshToken refreshToken = ((RefreshToken) request.getAttribute("REFRESH_TOKEN"));

		// 기존에 파싱된 access 토큰이 없을 때 파싱
		if (accessToken == null) {
			Cookie cookie = null;
			for (Cookie c : request.getCookies()) {
				if (c.getName().equals("ACCESS_TOKEN")) {
					cookie = c;
					break;
				}
			}
			if (cookie != null) {
				Map<String, Object> tokenInfo = jwtProvider.parseJwt(cookie.getValue(), Map.of("user_id", Long.class, "email", String.class, "role", JSONArray.class, "permissions", JSONArray.class));
				accessToken = new AccessToken(
						((Long) tokenInfo.get("user_id")),
						((String) tokenInfo.get("email")),
						((JSONArray) tokenInfo.get("role")).toList().stream().map(role -> Role.valueOf(role.toString())).toList(),
						((JSONArray) tokenInfo.get("permissions")).toList().stream().map(permission -> Permission.getValue(permission.toString())).toList()
				);
				request.setAttribute("ACCESS_TOKEN", accessToken);
				return true;
			}
		}

		// access 토큰이 존재하지 않을 때 refresh token 이용해 access token 생성
		if (accessToken == null) {
			if (refreshToken == null) {
				Cookie cookie = null;
				for (Cookie c : request.getCookies()) {
					if (c.getName().equals("REFRESH_TOKEN")) {
						cookie = c;
					}
				}
				if (cookie != null) {
					AccessAndRefreshTokenObjectDTO accessAndRefreshTokenObjectDTO = loginService.reLogin(cookie.getValue());
					accessToken = accessAndRefreshTokenObjectDTO.getAccessToken();
					refreshToken = accessAndRefreshTokenObjectDTO.getRefreshToken();
					Cookie[] cookies = loginService.generateCookie(accessAndRefreshTokenObjectDTO.getAccessTokenAsString(), accessAndRefreshTokenObjectDTO.getRefreshTokenAsString());
					for (int i = 0; i < cookies.length; i++) {
						response.addCookie(cookies[i]);
					}
					request.setAttribute("ACCESS_TOKEN", accessToken);
					request.setAttribute("REFRESH_TOKEN", refreshToken);
					return true;
				}
			}

			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			return false;
		}
		if (!accessToken.getPermission().contains(this.permission)) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			return false;
		}

		return true;
	}
}
