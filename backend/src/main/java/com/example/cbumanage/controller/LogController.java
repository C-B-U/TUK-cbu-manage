package com.example.cbumanage.controller;

import com.example.cbumanage.model.Log;
import com.example.cbumanage.service.LogService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class LogController {
    @Autowired
    private LogService logService;                     //로그 서비스 참조 선언

    @PostMapping("/api/v1/createLog")
    public String createLog(@RequestBody Log log){     //로그 생성 함수
        try{
            logService.createLog(log);
            return "로그 생성 성공!";
        }catch (Exception e) {
            return "로그 생성 실패!";
        }
    }
}
