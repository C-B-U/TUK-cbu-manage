package com.example.cbumanage.service;

import com.example.cbumanage.dto.SuccessCandidateDTO;
import com.example.cbumanage.model.SuccessCandidate;
import com.example.cbumanage.repository.CandidateManageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CandidateManageService {

    @Autowired
    CandidateManageRepository candidateManageRepository;
    public SuccessCandidate validateCandidate(SuccessCandidateDTO successCandidateDTO){
        return candidateManageRepository.findByStudentNumberAndNickName(successCandidateDTO.getStudentNumber(), successCandidateDTO.getNickName());
    }

}
