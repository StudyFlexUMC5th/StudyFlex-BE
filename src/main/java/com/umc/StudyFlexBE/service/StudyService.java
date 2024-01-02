package com.umc.StudyFlexBE.service;

import com.umc.StudyFlexBE.dto.response.BaseException;
import com.umc.StudyFlexBE.dto.response.BaseResponseStatus;
import com.umc.StudyFlexBE.dto.response.study.AuthorityType;
import com.umc.StudyFlexBE.entity.Study;
import com.umc.StudyFlexBE.repository.StudyParticipationRepository;
import com.umc.StudyFlexBE.repository.StudyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudyService {

    private final StudyRepository studyRepository;
    private final StudyParticipationRepository studyParticipationRepository;

    @Autowired
    public StudyService(StudyRepository studyRepository, StudyParticipationRepository studyParticipationRepository) {
        this.studyRepository = studyRepository;
        this.studyParticipationRepository = studyParticipationRepository;
    }

    public List<Study> getLatestStudies() {
        return studyRepository.findTop5ByOrderByCreatedAtDesc();
    }
    public List<Study> getOpenStudies() {
        return studyRepository.findByStatus("모집중"); // 또는 StudyStatus.모집중, enum 사용시
    }

    public void checkDuplicateStudyName(String name){
        if(studyRepository.existsByName(name)){
            throw new BaseException(BaseResponseStatus.DUPLICATE_STUDY_NAME);
        }
    }

    public AuthorityType checkAuthority(Long studyId, Long memberId){
        Study study = studyRepository.findById(studyId).orElseThrow(
                () -> new BaseException(BaseResponseStatus.NO_SUCH_STUDY)
        );

        if(!studyParticipationRepository.existsByStudy_idAndMember_id(studyId,memberId)){
            return AuthorityType.NON_MEMBER;
        }

        if(study.getLeaderId().equals(memberId)){
            return AuthorityType.LEADER;
        }else {
            return AuthorityType.MEMBER;
        }

    }

}
