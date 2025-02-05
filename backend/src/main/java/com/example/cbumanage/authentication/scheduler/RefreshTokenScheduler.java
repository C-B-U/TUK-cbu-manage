package com.example.cbumanage.authentication.scheduler;

import com.example.cbumanage.authentication.service.LoginService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenScheduler {

	private final LoginService loginService;

	public RefreshTokenScheduler(LoginService loginService) {
		this.loginService = loginService;

		loginService.clearRefreshToken();
	}

	@Scheduled(cron = "* * 3 * * *")
	public void run() {
		loginService.clearRefreshToken();
	}
}
