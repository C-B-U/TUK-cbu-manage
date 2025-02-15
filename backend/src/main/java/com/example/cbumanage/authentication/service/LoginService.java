package com.example.cbumanage.authentication.service;

import com.example.cbumanage.authentication.authorization.Permission;
import com.example.cbumanage.authentication.dto.*;
import com.example.cbumanage.authentication.entity.LoginEntity;
import com.example.cbumanage.authentication.entity.RefreshToken;
import com.example.cbumanage.authentication.exceptions.InvalidEmailException;
import com.example.cbumanage.authentication.exceptions.InvalidPasswordException;
import com.example.cbumanage.authentication.exceptions.MemberExistException;
import com.example.cbumanage.authentication.repository.LoginRepository;
import com.example.cbumanage.authentication.repository.RefreshTokenRepository;
import com.example.cbumanage.exception.MemberNotExistsException;
import com.example.cbumanage.model.CbuMember;
import com.example.cbumanage.model.SuccessCandidate;
import com.example.cbumanage.model.enums.Role;
import com.example.cbumanage.repository.CbuMemberRepository;
import com.example.cbumanage.repository.SuccessCandidateRepository;
import com.example.cbumanage.service.CandidateAppendService;
import com.example.cbumanage.utils.HashUtil;
import com.example.cbumanage.utils.JwtProvider;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

/**
 * LoginService는 로그인, 회원가입, 비밀번호 변경, 회원 삭제, 리프레시 토큰 재발급 및 정리와 관련된
 * 인증 및 사용자 관리 기능을 제공하는 서비스 클래스입니다.
 */
@Service
public class LoginService {

	// 이메일 관련 기능을 제공하는 매니저 (예: 이메일 유효성 검사)
	private final EmailManager emailManager;

	// CBU 회원 관련 데이터 접근을 위한 리포지토리
	private final CbuMemberRepository cbuMemberRepository;

	// 로그인 엔티티(사용자 인증 정보) 데이터 접근을 위한 리포지토리
	private final LoginRepository loginRepository;

	// 리프레시 토큰 데이터 접근을 위한 리포지토리
	private final RefreshTokenRepository refreshTokenRepository;

	// 회원가입 시 합격자 데이터 조회를 위한 리포지토리
	@Autowired
	SuccessCandidateRepository successCandidateRepository;

	// 합격자 정보를 구글 스프레드시트에 추가하는 서비스
	@Autowired
	CandidateAppendService candidateAppendService;

	// JWT 생성 및 검증을 위한 유틸리티
	private final JwtProvider jwtProvider;

	// 비밀번호 해싱 기능을 제공하는 유틸리티
	private final HashUtil hashUtil;

	// 비밀번호 해싱 시 사용할 salt 값
	private final String salt;

	// Access 토큰의 만료 시간 (초 단위)
	private final long accessTokenExpireTime;

	// Refresh 토큰의 만료 시간 (초 단위)
	private final long refreshTokenExpireTime;

	/**
	 * 생성자.
	 * 외부에서 주입받은 빈들과 프로퍼티를 이용하여 LoginService를 초기화합니다.
	 *
	 * @param emailManager 이메일 관련 매니저
	 * @param cbuMemberRepository CBU 회원 데이터 접근 리포지토리
	 * @param loginRepository 로그인 엔티티 리포지토리
	 * @param refreshTokenRepository 리프레시 토큰 리포지토리
	 * @param jwtProvider JWT 관련 유틸리티
	 * @param hashUtil 해싱 관련 유틸리티
	 * @param salt 비밀번호 해싱 시 사용할 salt 값 (프로퍼티 cbu.login.salt)
	 * @param accessTokenExpireTime Access 토큰 만료 시간 (밀리초 단위로 주입되어 초로 변환됨)
	 * @param refreshTokenExpireTime Refresh 토큰 만료 시간 (밀리초 단위로 주입되어 초로 변환됨)
	 */
	public LoginService(EmailManager emailManager,
						CbuMemberRepository cbuMemberRepository,
						LoginRepository loginRepository,
						RefreshTokenRepository refreshTokenRepository,
						JwtProvider jwtProvider,
						HashUtil hashUtil,
						@Value("${cbu.login.salt}") String salt,
						@Value("${cbu.jwt.expireTime}") Long accessTokenExpireTime,
						@Value("${cbu.jwt.refreshExpireTime}") Long refreshTokenExpireTime) {
		this.emailManager = emailManager;
		this.cbuMemberRepository = cbuMemberRepository;
		this.loginRepository = loginRepository;
		this.refreshTokenRepository = refreshTokenRepository;
		this.jwtProvider = jwtProvider;
		this.hashUtil = hashUtil;
		// salt 값이 null인 경우 기본값 사용
		this.salt = salt == null ? "p394huwtfp9q3a4" : salt;
		// 입력받은 만료 시간을 밀리초에서 초로 변환하여 저장
		this.accessTokenExpireTime = accessTokenExpireTime / 1000;
		this.refreshTokenExpireTime = refreshTokenExpireTime / 1000;
	}

	/**
	 * 로그인 메서드.
	 * 사용자가 이메일과 비밀번호를 제공하면, 해당 사용자의 로그인 엔티티를 조회하고, 비밀번호를 해싱하여 비교합니다.
	 * 만약 검증에 성공하면, Access 토큰과 Refresh 토큰을 생성하여 반환합니다.
	 *
	 * @param dto 이메일과 비밀번호 정보를 담은 DTO
	 * @return Access 및 Refresh 토큰 정보를 담은 DTO
	 * @throws MemberNotExistsException 이메일에 해당하는 회원이 없으면 예외 발생
	 * @throws InvalidPasswordException 비밀번호가 올바르지 않으면 예외 발생
	 */
	@Transactional
	public AccessAndRefreshTokenDTO login(final EmailAndPasswordDTO dto) {
		// 이메일로 로그인 엔티티 조회, 없으면 MemberNotExistsException 발생
		LoginEntity login = loginRepository.findByEmailEquals(dto.getEmail())
				.orElseThrow(MemberNotExistsException::new);
		// 입력한 비밀번호를 해싱한 값과 DB에 저장된 비밀번호를 비교
		if (!login.getPassword().equals(hashUtil.hash(dto.getPassword() + salt)))
			throw new InvalidPasswordException();

		// ADMIN 계정인 경우, 관리자 권한으로 토큰을 생성
		List<Role> adminRoles = List.of(Role.ADMIN);
		if (Objects.equals(dto.getEmail(), "cbuAdmin@tukorea.ac.kr")) {
			// 만료 시간을 현재 시간 + 86400000 밀리초 (하루)로 설정
			Long exp = jwtProvider.currentTime() + 86400000;
			// 새로운 리프레시 토큰 생성
			RefreshToken refreshToken = new RefreshToken(login.getUserId(), exp);
			// Access 토큰 생성 (관리자 역할 및 DB에 저장된 권한 사용)
			AccessToken accessToken = new AccessToken(login.getUserId(), login.getEmail(), adminRoles, login.getPermissions());
			// 리프레시 토큰 저장
			refreshTokenRepository.save(refreshToken);
			// 생성된 토큰을 이용해 AccessAndRefreshTokenDTO 반환
			return generateToken(accessToken, refreshToken);
		} else {
			// 일반 회원인 경우, CbuMember 정보 조회 후 토큰 생성
			CbuMember cbuMember = cbuMemberRepository.findById(login.getUserId())
					.orElseThrow(() -> new MemberNotExistsException("LoginEntity exists, but CbuMember doesn't exist"));
			Long exp = jwtProvider.currentTime() + 86400000;
			RefreshToken refreshToken = new RefreshToken(login.getUserId(), exp);
			// Access 토큰 생성 시, CbuMember의 역할과 로그인 엔티티에 저장된 권한 사용
			AccessToken accessToken = new AccessToken(login.getUserId(), login.getEmail(), cbuMember.getRole(), login.getPermissions());
			refreshTokenRepository.save(refreshToken);
			return generateToken(accessToken, refreshToken);
		}
	}

	/**
	 * reLogin 메서드: 기존의 Refresh 토큰을 사용하여 새로운 Access 토큰과 Refresh 토큰을 생성합니다.
	 *
	 * @param refreshToken 기존 Refresh 토큰 (JWT 문자열)
	 * @return 새로운 Access 토큰과 Refresh 토큰 정보를 포함한 객체
	 * @throws NoSuchElementException 만료되었거나 존재하지 않는 Refresh 토큰일 경우 예외 발생
	 */
	@Transactional
	public AccessAndRefreshTokenObjectDTO reLogin(final String refreshToken) {
		// refreshToken JWT를 파싱하여, 내부에 저장된 UUID 값을 추출
		Map<String, Object> tokenInfo = jwtProvider.parseJwt(refreshToken, Map.of("uuid", UUID.class));
		// 새로운 만료 시간 설정 (현재 시간 + 하루)
		Long exp = jwtProvider.currentTime() + 86400000;
		// 데이터베이스에서 해당 UUID의 RefreshToken을 조회
		RefreshToken refresh = refreshTokenRepository.findById(((UUID) tokenInfo.get("uuid")))
				.orElseThrow(() -> new NoSuchElementException("There is no refresh token"));
		// 해당 RefreshToken에 연결된 LoginEntity 조회, 없으면 예외 발생
		LoginEntity login = loginRepository.findById(refresh.getUserId())
				.orElseThrow(MemberNotExistsException::new);
		// CbuMember 조회 (LoginEntity는 존재하지만 CbuMember가 없으면 예외 발생)
		CbuMember cbuMember = cbuMemberRepository.findById(refresh.getUserId())
				.orElseThrow(MemberNotExistsException::new);
		// 새로운 Access 토큰 생성 (CbuMember의 역할과 로그인 엔티티에 저장된 권한 사용)
		AccessToken access = new AccessToken(login.getUserId(), login.getEmail(), cbuMember.getRole(), login.getPermissions());
		// 기존 RefreshToken의 만료 시간을 갱신
		refresh.setExp(exp);

		// 새로운 토큰을 생성하여 AccessAndRefreshTokenDTO로 반환
		AccessAndRefreshTokenDTO accessAndRefreshTokenDTO = generateToken(access, refresh);
		return new AccessAndRefreshTokenObjectDTO(
				access,
				refresh,
				accessAndRefreshTokenDTO.getAccessToken(),
				accessAndRefreshTokenDTO.getRefreshToken()
		);
	}

	/**
	 * generateToken 메서드는 AccessToken과 RefreshToken을 기반으로 JWT 문자열을 생성합니다.
	 *
	 * @param accessToken Access 토큰 객체
	 * @param refreshToken Refresh 토큰 객체
	 * @return Access 및 Refresh 토큰 문자열을 포함하는 DTO
	 */
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
	 * generateCookie 메서드는 Access 토큰과 Refresh 토큰 문자열을 쿠키로 생성합니다.
	 * 쿠키 배열의 인덱스 0에는 Access 토큰, 1에는 Refresh 토큰이 저장됩니다.
	 *
	 * @param accessToken JWT 문자열 형식의 Access 토큰
	 * @param refreshToken JWT 문자열 형식의 Refresh 토큰
	 * @return 생성된 쿠키 배열
	 */
	public Cookie[] generateCookie(String accessToken, String refreshToken) {
		Cookie accessTokenCookie = new Cookie("ACCESS_TOKEN", accessToken);
		accessTokenCookie.setSecure(true); // HTTPS 연결에서만 전송하도록 설정
		accessTokenCookie.setMaxAge(((int) accessTokenExpireTime));

		Cookie refreshTokenCookie = new Cookie("REFRESH_TOKEN", refreshToken);
		refreshTokenCookie.setSecure(true); // HTTPS 연결에서만 전송하도록 설정
		refreshTokenCookie.setMaxAge(((int) this.refreshTokenExpireTime));

		return new Cookie[]{accessTokenCookie, refreshTokenCookie};
	}

	/**
	 * 회원가입(create) 메서드.
	 * 새로운 회원가입 요청을 처리하며, 이메일 및 회원 정보를 검증하고 저장합니다.
	 *
	 * @param dto 회원가입 요청 정보를 담은 DTO (email, password, name, studentNumber, nickname)
	 * @return 생성된 LoginEntity 객체
	 * @throws IOException 예외 발생 시 전달
	 * @throws MemberExistException 만약 해당 이메일이 이미 존재하면 예외 발생
	 * @throws MemberNotExistsException 회원 정보가 존재하지 않을 경우 예외 발생
	 */
	@Transactional
	public LoginEntity create(SignUpRequestDTO dto) throws IOException {
		// 이메일 유효성 검사: 이메일 도메인 등의 규칙에 맞는지 확인
		if (!emailManager.validEmail(dto.getEmail()))
			throw new InvalidEmailException();

		// 이메일 중복 체크: 이미 해당 이메일이 존재하는지 확인
		if (loginRepository.findByEmailEquals(dto.getEmail()).isPresent())
			throw new MemberExistException("The email is already in use");

		// 학생 번호로 CbuMember 객체를 조회 (회원 정보가 존재해야 함)
		CbuMember cbuMember = cbuMemberRepository.findByStudentNumber(dto.getStudentNumber())
				.orElseThrow(() -> new MemberNotExistsException("No member exists with student number"));
		// 이름 비교: 입력한 이름과 CbuMember에 저장된 이름이 일치해야 함
		if (!dto.getName().equals(cbuMember.getName()))
			throw new MemberNotExistsException("No 'CbuMember' object exists with the given name");

		// 새로운 LoginEntity 객체 생성:
		// - userId는 CbuMember의 고유 ID를 사용
		// - 이메일 및 해시된 비밀번호, 기본 권한(Member)을 설정
		LoginEntity entity = new LoginEntity(
				cbuMember.getCbuMemberId(),
				dto.getEmail(),
				hashUtil.hash(dto.getPassword() + salt),
				List.of(Permission.MEMBER)
		);
		entity = loginRepository.save(entity);

		// 성공 후보자 정보를 조회하여, 구글 스프레드시트에 추가합니다.
		SuccessCandidate successCandidate = successCandidateRepository.findByStudentNumber(dto.getStudentNumber());
		candidateAppendService.appendSuccessCandidateToGoogleSheet(successCandidate);

		return entity;
	}

	/**
	 * 비밀번호 변경 메서드.
	 * 주어진 사용자 ID에 대해 새로운 비밀번호로 업데이트합니다.
	 *
	 * @param userId 변경할 사용자의 ID
	 * @param password 새 비밀번호
	 * @throws MemberNotExistsException 사용자가 존재하지 않으면 예외 발생
	 */
	@Transactional
	public void editPassword(final Long userId, final String password) {
		// userId로 LoginEntity 조회, 없으면 예외 발생
		LoginEntity entity = loginRepository.findById(userId).orElseThrow(MemberNotExistsException::new);
		// 새로운 비밀번호를 해싱 후 업데이트
		entity.setPassword(hashUtil.hash(password + this.salt));
	}

	/**
	 * 회원 탈퇴(삭제) 메서드.
	 * 주어진 사용자 ID에 해당하는 LoginEntity와 관련된 모든 리프레시 토큰을 삭제합니다.
	 *
	 * @param userId 삭제할 사용자의 ID
	 * @throws MemberNotExistsException 사용자가 존재하지 않으면 예외 발생
	 */
	@Transactional
	public void delete(final long userId) {
		// userId로 LoginEntity 조회, 없으면 예외 발생
		LoginEntity entity = loginRepository.findById(userId).orElseThrow(MemberNotExistsException::new);
		// 해당 사용자의 모든 리프레시 토큰을 조회
		List<RefreshToken> refreshTokens = refreshTokenRepository.findAllByUserId(userId);
		// 로그인 엔티티와 관련 리프레시 토큰 모두 삭제
		loginRepository.delete(entity);
		refreshTokenRepository.deleteAll(refreshTokens);
	}

	/**
	 * clearRefreshToken 메서드.
	 * 현재 시간보다 만료된 리프레시 토큰들을 모두 조회하여 삭제합니다.
	 */
	public void clearRefreshToken() {
		// 현재 시간보다 만료된 모든 리프레시 토큰을 조회
		List<RefreshToken> refreshTokens = refreshTokenRepository.findAllByExpLessThan(jwtProvider.currentTime());
		// 조회된 리프레시 토큰들을 삭제
		refreshTokenRepository.deleteAll(refreshTokens);
	}
}
