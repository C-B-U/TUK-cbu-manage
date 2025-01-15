package com.example.cbumanage.service;

import com.example.cbumanage.model.CbuMember;
import com.example.cbumanage.repository.CbuMemberRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CbuMemberSyncService {
    @Autowired
    CbuMemberRepository cbuMemberRepository;

    @Value("${google.spreadSheet.key}")
    private String SheetKey;                               //구글 스프레드 시트 사용 api 키 값

    @Value("${google.spreadSheet.id}")
    private String SheetId;                                //구글 스프레드 시트 아이디 값

    private static final String SheetName = "sheet";       //구글 스프레드 시트 시트 이름

    @Transactional
    public void syncMembersFromGoogleSheet() {                    //스프레드 시트 -> 데이터베이스 유저 데이터 주입
        List<CbuMember> sheetMembers = getMembersFromGoogleSheet();    //스프레드 시트에서 값을 가져와 members에 저장 후
        List<CbuMember> dbMembers = cbuMemberRepository.findAll();

        Map<Long, CbuMember> dbMemberMap = new HashMap<>();
        for (CbuMember member : dbMembers) {
            dbMemberMap.put(member.getStudentNumber(), member);
        }
        List<CbuMember> toUpdate = new ArrayList<>();
        List<CbuMember> toCreate = new ArrayList<>();

        for (CbuMember sheetMember : sheetMembers) {
            CbuMember existingMember = dbMemberMap.get(sheetMember.getStudentNumber());

            if (existingMember == null) {
                // 새로운 멤버
                toCreate.add(sheetMember);
            } else if (hasChanged(existingMember, sheetMember)) {
                // 기존 멤버 수정
                updateMemberFields(existingMember, sheetMember);
                toUpdate.add(existingMember);
            }
            // 변경이 없는 경우 아무 작업도 하지 않음
        }

        List<CbuMember> toDelete = findDeletedMembers(dbMemberMap, sheetMembers);

        // 변경사항 저장
        if (!toCreate.isEmpty()) {
            cbuMemberRepository.saveAll(toCreate);
        }
        if (!toUpdate.isEmpty()) {
            cbuMemberRepository.saveAll(toUpdate);
        }
        if (!toDelete.isEmpty()) {
            cbuMemberRepository.deleteAll(toDelete);
        }

//        cbuMemberRepository.saveAll(members);                     //레포지토리를 이용해 데이터베이스에 members에 저장
    }

    private boolean hasChanged(CbuMember dbMember, CbuMember sheetMember) {
        return !Objects.equals(dbMember.getName(), sheetMember.getName()) ||
                !Objects.equals(dbMember.getPhoneNumber(), sheetMember.getPhoneNumber()) ||
                !Objects.equals(dbMember.getMajor(), sheetMember.getMajor()) ||
                !Objects.equals(dbMember.getGrade(), sheetMember.getGrade()) ||
                !Objects.equals(dbMember.getGeneration(), sheetMember.getGeneration()) ||
                !Objects.equals(dbMember.getNote(), sheetMember.getNote()) ||
                !Objects.equals(dbMember.getDue(), sheetMember.getDue());
    }

    private void updateMemberFields(CbuMember existing, CbuMember updated) {
        existing.setName(updated.getName());
        existing.setPhoneNumber(updated.getPhoneNumber());
        existing.setMajor(updated.getMajor());
        existing.setGrade(updated.getGrade());
        existing.setGeneration(updated.getGeneration());
        existing.setNote(updated.getNote());
        existing.setDue(updated.getDue());
    }

    private List<CbuMember> findDeletedMembers(Map<Long, CbuMember> dbMembers, List<CbuMember> sheetMembers) {
        Set<Long> sheetStudentNumbers = sheetMembers.stream()
                .map(CbuMember::getStudentNumber)
                .collect(Collectors.toSet());

        return dbMembers.values().stream()
                .filter(dbMember -> !sheetStudentNumbers.contains(dbMember.getStudentNumber()))
                .collect(Collectors.toList());
    }

    private List<CbuMember> getMembersFromGoogleSheet() {               //스프레드 시트 데이터 가져오는 함수
        URI sheetUrl = getSheetUri();                                   //커스텀 URI를 생성
        RestTemplate rt = new RestTemplate();
        ResponseEntity<GoogleSheetResponse> response = rt.exchange(     //Get 메소드를 이용해 구글에 요청 전송
                sheetUrl,
                HttpMethod.GET,
                null,
                GoogleSheetResponse.class
        );

        GoogleSheetResponse sheetResponse = response.getBody();         //응답에서 body값을 가져와서
        List<List<Object>> values = sheetResponse.getValues();

        List<CbuMember> members = new ArrayList<>();                    //arrayList로 매핑
        for (int i = 1; i < values.size(); i++) {
            List<Object> row = values.get(i);
            CbuMember member = mapRowToMember(row);
            members.add(member);
        }

        return members;
    }

    private URI getSheetUri() {       //api key, 스프레드 시트 아이디와 이름을 URI에 주입
        return UriComponentsBuilder
                .fromUriString("https://sheets.googleapis.com/v4/spreadsheets/{sheetId}/values/{sheetName}")
                .queryParam("key", SheetKey)
                .buildAndExpand(SheetId, SheetName)
                .toUri();
    }

    private CbuMember mapRowToMember(List<Object> row) {
        CbuMember member = new CbuMember();
        member.setName(getStringValue(row, 0));
        member.setRole(List.of());
        member.setPhoneNumber(getStringValue(row, 1));
        member.setMajor(getStringValue(row, 2));
        member.setGrade(getStringValue(row, 3));
        member.setStudentNumber(getLongValue(row, 4));
        member.setGeneration(getLongValue(row, 5));
        member.setNote(getStringValue(row, 6));
        member.setDue(getStringValue(row, 7));
        return member;
    }

    private String getStringValue(List<Object> row, int index) {
        if (index >= row.size() || row.get(index) == null || row.get(index).toString().trim().isEmpty()) {
            return null;
        }
        return row.get(index).toString();
    }

    private Long getLongValue(List<Object> row, int index) {
        String value = getStringValue(row, index);
        return value != null ? Long.parseLong(value) : null;
    }

//    private CbuMember mapRowToMember(List<Object> row) {     //멤버 데이터값을 String 값으로 변환해 매핑 (스프레드시트에 빈칸이 있으면 작동이 멈춤 해결방법 찾는중,,,)
//        CbuMember member = new CbuMember();
//        member.setName((String) row.get(0));
//        member.setRole(List.of());
//        member.setPhoneNumber((String) row.get(1));
//        member.setMajor((String) row.get(2));
//        member.setGrade((String) row.get(3));
//        member.setStudentNumber(Long.parseLong(row.get(4).toString()));
//        member.setGeneration(Long.parseLong(row.get(5).toString()));
//        member.setNote(row.get(6) != "" ? (String) row.get(6) : null);
//        member.setDue(row.get(7) != "" ? (String) row.get(7) : null);
//        return member;
//    }
}

@Getter
class GoogleSheetResponse {
    private List<List<Object>> values;

    public void setValues(List<List<Object>> values) {
        this.values = values;
    }
}

