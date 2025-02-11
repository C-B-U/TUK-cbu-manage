package com.example.cbumanage.authentication.controller;

import com.example.cbumanage.authentication.dto.*;
import com.example.cbumanage.authentication.exceptions.AuthenticationException;
import com.example.cbumanage.authentication.exceptions.InvalidJwtException;
import com.example.cbumanage.authentication.intercepter.AuthenticationInterceptor;
import com.example.cbumanage.authentication.service.LoginService;
import com.example.cbumanage.utils.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/v1/login")
@Tag(name = "로그인 컨트롤러", description = "")
public class LoginController {

	private final LoginService loginService;

	private final AuthenticationInterceptor authenticationInterceptor;

	public LoginController(LoginService loginService, JwtProvider jwtProvider) {
		this.loginService = loginService;

		authenticationInterceptor = new AuthenticationInterceptor(null, loginService, jwtProvider);
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "로그인 후 쿠키에 토큰 반환", description = "헤더에 이메일과 비밀번호를 넣어 요청")
	public void login(@RequestHeader("email") @Email final String email, @RequestHeader("password") final String password, HttpServletResponse res) {
		AccessAndRefreshTokenDTO login = loginService.login(new EmailAndPasswordDTO(email, password));

		Cookie[] cookies = loginService.generateCookie(login.getAccessToken(), login.getRefreshToken());
		for (Cookie cookie : cookies) {
			res.addCookie(cookie);
		}
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "회원가입", description = "json 형식으로 email, password, name, studentNumber, nickname을 넣어 요청")
	public void register(@RequestBody @Valid SignUpRequestDTO dto) {
		loginService.create(dto);
	}

	@PatchMapping("/password")
	@Operation(summary = "비밀번호 변경")
	@ResponseStatus(HttpStatus.OK)
	public void editPassword(@RequestBody @Valid UserIdAndPasswordDTO dto) {
		loginService.editPassword(dto.getUserId(), dto.getPassword());
	}

	@DeleteMapping
	@Operation(summary = "로그인 엔티티 삭제")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@RequestHeader("USER_ID") Long userId, HttpServletRequest request, HttpServletResponse response) {
		checkToken(request, response, userId);

		loginService.delete(userId);
	}

	private void checkToken(final HttpServletRequest request, final HttpServletResponse response, final Long userId) {
		try {
			authenticationInterceptor.preHandle(request, response, null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AuthenticationException(e.getClass().getName() + " / " + e.getMessage());
		}
		AccessToken accessToken = ((AccessToken) request.getAttribute("ACCESS_TOKEN"));
		if (accessToken == null || !accessToken.getUserId().equals(userId)) {
			throw new InvalidJwtException();
		}
	}
}
