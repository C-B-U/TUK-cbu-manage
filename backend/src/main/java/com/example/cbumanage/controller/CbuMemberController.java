package com.example.cbumanage.controller;

import com.example.cbumanage.authentication.dto.AccessToken;
import com.example.cbumanage.authentication.exceptions.AuthenticationException;
import com.example.cbumanage.dto.MemberCreateDTO;
import com.example.cbumanage.dto.MemberDTO;
import com.example.cbumanage.dto.MemberUpdateDTO;
import com.example.cbumanage.model.CbuMember;
import com.example.cbumanage.model.enums.Role;
import com.example.cbumanage.repository.CbuMemberRepository;
import com.example.cbumanage.service.CbuMemberManageService;
import com.example.cbumanage.service.CbuMemberSyncService;
import com.example.cbumanage.utils.CbuMemberMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/member")
public class CbuMemberController {
    private final CbuMemberSyncService cbuMemberSyncService;
    private final CbuMemberManageService cbuMemberManageService;
    private final CbuMemberRepository cbuMemberRepository;
    private final CbuMemberMapper cbuMemberMapper;

    public CbuMemberController(CbuMemberSyncService cbuMemberSyncService, CbuMemberManageService cbuMemberManageService, CbuMemberRepository cbuMemberRepository, CbuMemberMapper cbuMemberMapper) {
        this.cbuMemberSyncService = cbuMemberSyncService;         //서비스 참조 선언
        this.cbuMemberManageService = cbuMemberManageService;     //서비스 참조 선언
        this.cbuMemberRepository = cbuMemberRepository;           //레포지토리 참조 선언
        this.cbuMemberMapper = cbuMemberMapper;
    }

    @PostMapping("s/sync")
    public String memberSync() {
        cbuMemberSyncService.syncMembersFromGoogleSheet();      //스프레드시트에서 데이터베이스로 데이터 값 주입
        return "멤버 저장 성공!";
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MemberDTO getMember(@PathVariable Long id, AccessToken accessToken) {
        if (!accessToken.getRole().contains(Role.ADMIN)) throw new AuthenticationException("You don't have permission");
        CbuMember cbuMember = cbuMemberRepository.findById(id).orElseThrow();
        return cbuMemberMapper.map(cbuMember);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long postMember(@RequestBody @Valid MemberCreateDTO memberCreateDTO, AccessToken accessToken){
//        accessToken.
        CbuMember member = cbuMemberManageService.createMember(memberCreateDTO);
        return member.getCbuMemberId();
    }

    // TODO : MemberUpdateDTO validation 추가
    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void patchMember(@RequestBody MemberUpdateDTO memberDTO, AccessToken accessToken) {
        if (!accessToken.getRole().contains(Role.ADMIN)) throw new AuthenticationException("You don't have permission");
        cbuMemberManageService.updateUser(memberDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteMember(@PathVariable Long id) {
        cbuMemberManageService.deleteMember(id);
    }

    @GetMapping("s")
    public ResponseEntity<List<MemberDTO>> getMembers(@RequestParam(name = "page", required = false) Integer page, final AccessToken accessToken) {
        if (!accessToken.getRole().contains(Role.ADMIN)) throw new AuthenticationException("You don't have permission");
        if (page == null) page = 0;
        return ResponseEntity.ok(cbuMemberMapper.map(cbuMemberManageService.getMembers(page)));
    }

    @ExceptionHandler(RestClientException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String restClientException(RestClientException e) {
        return "Fail to request data while RestTemplate";
    }
}
