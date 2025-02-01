package com.example.cbumanage.authentication.service;

import com.example.cbumanage.authentication.authorization.Permission;
import com.example.cbumanage.authentication.dto.*;
import com.example.cbumanage.authentication.entity.LoginEntity;
import com.example.cbumanage.authentication.entity.RefreshToken;
import com.example.cbumanage.authentication.exceptions.InvalidEmailException;
import com.example.cbumanage.authentication.exceptions.InvalidPasswordException;
import com.example.cbumanage.authentication.exceptions.MemberExistException;
import com.example.cbumanage.authentication.repository.RefreshTokenRepository;
import com.example.cbumanage.exception.MemberNotExistsException;
import com.example.cbumanage.authentication.repository.LoginRepository;
import com.example.cbumanage.model.CbuMember;
import com.example.cbumanage.repository.CbuMemberRepository;
import com.example.cbumanage.utils.HashUtil;
import com.example.cbumanage.utils.JwtProvider;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class LoginService {

	private final EmailManager emailManager;

	private final CbuMemberRepository cbuMemberRepository;
	private final LoginRepository loginRepository;
	private final RefreshTokenRepository refreshTokenRepository;

	private final JwtProvider jwtProvider;
	private final HashUtil hashUtil;
	private final String salt;

	private final long accessTokenExpireTime;
	private final long refreshTokenExpireTime;

	public LoginService(EmailManager emailManager, CbuMemberRepository cbuMemberRepository, LoginRepository loginRepository, RefreshTokenRepository refreshTokenRepository, JwtProvider jwtProvider, HashUtil hashUtil, @Value("${cbu.login.salt}") String salt, @Value("${cbu.jwt.expireTime}") Long accessTokenExpireTime, @Value("${cbu.jwt.refreshExpireTime}") Long refreshTokenExpireTime) {
		this.emailManager = emailManager;
		this.cbuMemberRepository = cbuMemberRepository;
		this.loginRepository = loginRepository;
		this.refreshTokenRepository = refreshTokenRepository;
		this.jwtProvider = jwtProvider;
		this.hashUtil = hashUtil;
		this.salt = salt == null ? "p394huwtfp9q3a4" : salt;
		this.accessTokenExpireTime = accessTokenExpireTime / 1000;
		this.refreshTokenExpireTime = refreshTokenExpireTime / 1000;
	}

	/**
	 * @throws MemberNotExistsException No member match with 'email'
	 * @throws InvalidPasswordException When 'password' is incorrect
	 */
	@Transactional
	public AccessAndRefreshTokenDTO login(final EmailAndPasswordDTO dto) {
		// validation check
		LoginEntity login = loginRepository.findByEmailEquals(dto.getEmail()).orElseThrow(MemberNotExistsException::new);
		if (!login.getPassword().equals(hashUtil.hash(dto.getPassword() + salt))) throw new InvalidPasswordException();

		// generate access token and refresh token
		CbuMember cbuMember = cbuMemberRepository.findById(login.getUserId()).orElseThrow(() -> new MemberNotExistsException("LoginEntity is exists, but CbuMember isn't exist"));
		Long exp = jwtProvider.currentTime() + 86400000;
		RefreshToken refreshToken = new RefreshToken(login.getUserId(), exp);
		AccessToken accessToken = new AccessToken(login.getUserId(), login.getEmail(), cbuMember.getRole(), login.getPermissions());
		return generateToken(accessToken, refreshToken);
	}

	/**
	 * Generates a new access token and refresh token using existing refresh token.
	 */
	@Transactional
	public AccessAndRefreshTokenObjectDTO reLogin(final String refreshToken) {
		// Validation check
		Map<String, Object> tokenInfo = jwtProvider.parseJwt(refreshToken, Map.of("uuid", UUID.class));

		Long exp = jwtProvider.currentTime() + 86400000;
		RefreshToken refresh = refreshTokenRepository.findById(((UUID) tokenInfo.get("uuid"))).orElseThrow();
		LoginEntity login = loginRepository.findById(refresh.getUserId()).orElseThrow(MemberNotExistsException::new);
		CbuMember cbuMember = cbuMemberRepository.findById(refresh.getUserId()).orElseThrow(MemberNotExistsException::new);
		AccessToken access = new AccessToken(login.getUserId(), login.getEmail(), cbuMember.getRole(), login.getPermissions());
		refresh.setExp(exp);

		AccessAndRefreshTokenDTO accessAndRefreshTokenDTO = generateToken(access, refresh);
		return new AccessAndRefreshTokenObjectDTO(access, refresh, accessAndRefreshTokenDTO.getAccessToken(), accessAndRefreshTokenDTO.getRefreshToken());
	}

	public AccessAndRefreshTokenDTO generateToken(AccessToken accessToken, RefreshToken refreshToken) {
		return new AccessAndRefreshTokenDTO(
				jwtProvider.generateJwt("JWT", Map.of(
						"user_id", accessToken.getUserId(),
						"email", accessToken.getEmail(),
						"role", accessToken.getRole(),
						"permissions", accessToken.getPermission()
				)),
				jwtProvider.generateJwt("JWT", Map.of(
						"user_id", refreshToken.getUserId(),
						"uuid", refreshToken.getId(),
						"exp", refreshToken.getExp()
				))
		);
	}

	/**
	 *
	 * @param accessToken
	 * @param refreshToken
	 * @return Return array of cookies. Index 0 is access token and 1 is refresh token
	 */
	public Cookie[] generateCookie(String accessToken, String refreshToken) {
		Cookie accessTokenCookie = new Cookie("ACCESS_TOKEN", accessToken);
		accessTokenCookie.setSecure(true);
		accessTokenCookie.setMaxAge(((int) accessTokenExpireTime));

		Cookie refreshTokenCookie = new Cookie("REFRESH_TOKEN", refreshToken);
		refreshTokenCookie.setSecure(true);
		refreshTokenCookie.setMaxAge(((int) this.refreshTokenExpireTime));

		return new Cookie[]{accessTokenCookie, refreshTokenCookie};
	}

	/**
	 * @throws MemberExistException When member exist match with email;
	 */
	@Transactional
	public LoginEntity create(SignUpRequestDTO dto) {
		// Check email (domain check)
		if (!emailManager.validEmail(dto.getEmail())) throw new InvalidEmailException();

		// Check if email is already in use
		if (loginRepository.findByEmailEquals(dto.getEmail()).isPresent()) throw new MemberExistException("The email is already in use");

		// Check and get 'CbuMember' object
		CbuMember cbuMember = cbuMemberRepository.findByStudentNumber(dto.getStudentNumber()).orElseThrow(() -> new MemberNotExistsException("No member exists with student number"));
		if (!dto.getName().equals(cbuMember.getName())) throw new MemberNotExistsException("No 'CbuMember' object exists with the given name");

		LoginEntity entity = new LoginEntity(cbuMember.getCbuMemberId(), dto.getEmail(), hashUtil.hash(dto.getPassword() + salt), List.of(Permission.MEMBER));
		entity = loginRepository.save(entity);

		return entity;
	}

	/**
	 * @throws MemberNotExistsException No member match with 'userId'
	 */
	@Transactional
	public void editPassword(final Long userId, final String password) {
		LoginEntity entity = loginRepository.findById(userId).orElseThrow(MemberNotExistsException::new);
		entity.setPassword(hashUtil.hash(password + this.salt));
	}

	/**
	 * @throws MemberNotExistsException No member match with 'userId'
	 */
	@Transactional
	public void delete(final long userId) {
		LoginEntity entity = loginRepository.findById(userId).orElseThrow(MemberNotExistsException::new);
		loginRepository.delete(entity);
	}

	public void clearRefreshToken() {
		List<RefreshToken> refreshTokens = refreshTokenRepository.findAllByExpLessThan(jwtProvider.currentTime());
		refreshTokenRepository.deleteAll(refreshTokens);
	}
}
