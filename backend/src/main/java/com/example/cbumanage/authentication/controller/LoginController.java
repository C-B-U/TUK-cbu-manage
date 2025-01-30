package com.example.cbumanage.authentication.controller;

import com.example.cbumanage.authentication.dto.AccessAndRefreshTokenDTO;
import com.example.cbumanage.authentication.dto.EmailAndPasswordDTO;
import com.example.cbumanage.authentication.dto.SignUpRequestDTO;
import com.example.cbumanage.authentication.dto.UserIdAndPasswordDTO;
import com.example.cbumanage.authentication.service.LoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/login")
public class LoginController {

	private final LoginService loginService;

	public LoginController(LoginService loginService) {
		this.loginService = loginService;
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public void login(@RequestHeader("email") @Email final String email, @RequestHeader("password") @Size(min = 8) final String password, HttpServletResponse res) {
		AccessAndRefreshTokenDTO login = loginService.login(new EmailAndPasswordDTO(email, password));

		Cookie[] cookies = loginService.generateCookie(login.getAccessToken(), login.getRefreshToken());
		for (int i = 0; i < cookies.length; i++) {
			res.addCookie(cookies[i]);
		}
	}

	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	public void register(@RequestBody SignUpRequestDTO dto) {
		loginService.create(dto);
	}

	@PatchMapping("/password")
	public void editPassword(@RequestBody @Valid UserIdAndPasswordDTO dto) {
		loginService.editPassword(dto.getUserId(), dto.getPassword());
	}

	@DeleteMapping
	public void delete(@RequestHeader("USER_ID") final Long userId) {
		loginService.delete(userId);
	}
}
