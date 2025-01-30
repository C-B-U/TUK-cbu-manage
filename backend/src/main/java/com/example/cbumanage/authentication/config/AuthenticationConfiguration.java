package com.example.cbumanage.authentication.config;

import com.example.cbumanage.authentication.authorization.Permission;
import com.example.cbumanage.authentication.intercepter.AuthenticationInterceptor;
import com.example.cbumanage.authentication.repository.RefreshTokenRepository;
import com.example.cbumanage.authentication.service.LoginService;
import com.example.cbumanage.utils.JwtProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthenticationConfiguration implements WebMvcConfigurer {

	private final boolean enabled;
	private final AuthenticationHandlerMethodArgumentResolver authenticationHandlerMethodArgumentResolver;
	private final LoginService loginService;
	private final JwtProvider jwtProvider;

	public AuthenticationConfiguration(@Value("${cbu.jwt.interceptor}") boolean enabled, RefreshTokenRepository refreshTokenRepository, LoginService loginService, JwtProvider jwtProvider) {
		this.enabled = enabled;
		authenticationHandlerMethodArgumentResolver = new AuthenticationHandlerMethodArgumentResolver();
		this.loginService = loginService;
		this.jwtProvider = jwtProvider;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		if (!enabled) return;

		for (Permission p : Permission.values()) {
			if (p.getPath().isEmpty()) continue;

			InterceptorRegistration interceptorRegistration = registry.addInterceptor(new AuthenticationInterceptor(p, loginService, jwtProvider));
			p.getPath().forEach(interceptorRegistration::addPathPatterns);
			p.getExclusivePath().forEach(interceptorRegistration::excludePathPatterns);
		}
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(authenticationHandlerMethodArgumentResolver);
	}
}
