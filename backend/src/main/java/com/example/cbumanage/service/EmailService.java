package com.example.cbumanage.service;

import com.example.cbumanage.authentication.entity.LoginEntity;
import com.example.cbumanage.authentication.repository.LoginRepository;
import com.example.cbumanage.dto.EmailAuthResponseDTO;
import com.example.cbumanage.dto.MemberMailUpdateDTO;
import com.example.cbumanage.model.CbuMember;
import com.example.cbumanage.repository.CbuMemberRepository;
import com.example.cbumanage.utils.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${spring.mail.username}")
    private String senderEmail;

    private final JavaMailSender mailSender;
    private final RedisUtil redisUtil;

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    CbuMemberRepository cbuMemberRepository;

    public EmailAuthResponseDTO sendEmail(String toEmail) {
        if (redisUtil.existData(toEmail)) {
            redisUtil.deleteData(toEmail);
        }

        try {
            MimeMessage emailForm = createEmailForm(toEmail);
            mailSender.send(emailForm);
            return new EmailAuthResponseDTO(true, "인증번호가 메일로 전송되었습니다.");
        } catch (MessagingException | MailSendException e) {
            String errorr = e.getMessage();
            return new EmailAuthResponseDTO(false, errorr);
        }
    }

    private MimeMessage createEmailForm(String email) throws MessagingException {

        String authCode = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));

        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(senderEmail);
        message.setRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("인증코드입니다.");
        message.setText(setContext(authCode), "utf-8", "html");

        redisUtil.setDataExpire(email, authCode, 10 * 60L); // 10분

        return message;
    }

    private String setContext(String authCode) {
        String body = "";
        body += "<h4>" + "인증 코드를 입력하세요." + "</h4>";
        body += "<h2>" + "[" + authCode + "]" + "</h2>";
        return body;
    }

    public EmailAuthResponseDTO validateAuthCode(String email, String authCode) {
        String findAuthCode = redisUtil.getData(email);
        if (findAuthCode == null) {
            return new EmailAuthResponseDTO(false, "인증번호가 만료되었습니다. 다시 시도해주세요.");
        }

        if (findAuthCode.equals(authCode)) {
            return new EmailAuthResponseDTO(true, "인증에 성공했습니다.");

        } else {
            return new EmailAuthResponseDTO(false, "인증번호가 일치하지 않습니다.");
        }
    }

    @Transactional
    public void updateUserMail(MemberMailUpdateDTO memberMailUpdateDTO){
        LoginEntity loginEntity = loginRepository.findLoginEntityByStudentNumber(memberMailUpdateDTO.getStudentNumber());
        CbuMember cbuMember = cbuMemberRepository.findCbuMemberByStudentNumber(memberMailUpdateDTO.getStudentNumber());

        cbuMember.setEmail(memberMailUpdateDTO.getEmail());
        loginEntity.setEmail(memberMailUpdateDTO.getEmail());
        loginRepository.save(loginEntity);
        cbuMemberRepository.save(cbuMember);
    }

}