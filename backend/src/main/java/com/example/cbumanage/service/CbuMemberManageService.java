package com.example.cbumanage.service;


import com.example.cbumanage.dto.MemberCreateDTO;
import com.example.cbumanage.dto.MemberUpdateDTO;
import com.example.cbumanage.exception.MemberDoesntHavePermissionException;
import com.example.cbumanage.exception.MemberNotExistsException;
import com.example.cbumanage.model.CbuMember;
import com.example.cbumanage.model.enums.Role;
import com.example.cbumanage.repository.CbuMemberRepository;
import com.example.cbumanage.repository.DuesRepository;
import com.example.cbumanage.repository.LogRepository;
import com.example.cbumanage.utils.CbuMemberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CbuMemberManageService {

	private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

	private CbuMemberRepository memberRepository;
	private DuesRepository duesRepository;
	private LogRepository logRepository;
	private CbuMemberMapper cbuMemberMapper;

	@Autowired
	public CbuMemberManageService(CbuMemberRepository memberRepository, DuesRepository duesRepository, LogRepository logRepository, CbuMemberMapper cbuMemberMapper) {
		this.memberRepository = memberRepository;
		this.duesRepository = duesRepository;
		this.logRepository = logRepository;
		this.cbuMemberMapper = cbuMemberMapper;
	}

	@Transactional
	public List<CbuMember> getMembers(int page) {
		Page<CbuMember> memberPage = memberRepository.findAll(PageRequest.of(page, 10));
		return memberPage.getContent();
	}
	@Transactional
	public List<CbuMember> getMembersWithoutDues(final String term) {
		return memberRepository.findAllWithoutDues(term);
	}

	@Transactional
	public CbuMember createMember(final MemberCreateDTO memberCreateDTO) {
//		CbuMember adminMember = memberRepository.findById(adminMemberId).orElseThrow(() -> new MemberNotExistsException("There is no admin Member"));
//		if (!adminMember.getRole().contains(Role.ADMIN)) throw new MemberDoesntHavePermissionException("admin member (adminMemberId) doesn't have admin permission");

		// 저장
		CbuMember member = cbuMemberMapper.map(memberCreateDTO);
		memberRepository.save(member);

		return member;
	}

	@Transactional
	public void updateUser(MemberUpdateDTO memberUpdateDTO) {
		CbuMember cbuMember = memberRepository.findById(memberUpdateDTO.getCbuMemberId()).orElseThrow(MemberNotExistsException::new);

		// 업데이트
		cbuMemberMapper.map(memberUpdateDTO, cbuMember);
	}

	@Transactional
	public void deleteMember(final Long memberId) {
		memberRepository.deleteById(memberId);
	}
}
