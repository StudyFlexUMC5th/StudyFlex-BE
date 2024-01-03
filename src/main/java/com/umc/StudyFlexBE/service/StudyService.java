package com.umc.StudyFlexBE.service;

import com.umc.StudyFlexBE.dto.response.BaseException;
import com.umc.StudyFlexBE.dto.response.BaseResponseStatus;
import com.umc.StudyFlexBE.dto.response.StudyAuthorityType;
import com.umc.StudyFlexBE.entity.Member;
import com.umc.StudyFlexBE.entity.Study;
import com.umc.StudyFlexBE.entity.StudyParticipation;
import com.umc.StudyFlexBE.repository.MemberRepository;
import com.umc.StudyFlexBE.repository.StudyParticipationRepository;
import com.umc.StudyFlexBE.repository.StudyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudyService {

    private final StudyRepository studyRepository;
    private final StudyParticipationRepository studyParticipationRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public StudyService(StudyRepository studyRepository, StudyParticipationRepository studyParticipationRepository, MemberRepository memberRepository) {
        this.studyRepository = studyRepository;
        this.studyParticipationRepository = studyParticipationRepository;
        this.memberRepository = memberRepository;
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

    public StudyAuthorityType checkAuthority(Long studyId, Long memberId){
        Study study = studyRepository.findById(studyId).orElseThrow(
                () -> new BaseException(BaseResponseStatus.INTERNAL_SERVER_ERROR)
        );

        if(!studyParticipationRepository.existsByStudy_idAndMember_id(studyId,memberId)){
            return StudyAuthorityType.NON_MEMBER;
        }

        if(study.getLeaderId().equals(memberId)){
            return StudyAuthorityType.LEADER;
        }else {
            return StudyAuthorityType.MEMBER;
        }

    }

    public void participation(Long studyId, Member member){

        Study study = studyRepository.findById(studyId).orElseThrow(
                () -> new BaseException(BaseResponseStatus.NO_SUCH_STUDY)
        );

        StudyParticipation studyParticipation = StudyParticipation.builder()
                .study_id(study)
                .member_id(member)
                .build();

        studyParticipationRepository.save(studyParticipation);
    }
}
