package com.umc.StudyFlexBE.service;

import com.umc.StudyFlexBE.dto.request.StudyReq;
import com.umc.StudyFlexBE.dto.response.BaseException;
import com.umc.StudyFlexBE.dto.response.BaseResponseStatus;
import com.umc.StudyFlexBE.dto.response.StudyAuthorityType;
import com.umc.StudyFlexBE.entity.*;
import com.umc.StudyFlexBE.repository.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StudyService {

    private final StudyRepository studyRepository;
    private final StudyParticipationRepository studyParticipationRepository;
    private final StudyNoticeRepository studyNoticeRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public StudyService(StudyRepository studyRepository, StudyParticipationRepository studyParticipationRepository, StudyNoticeRepository studyNoticeRepository, CategoryRepository categoryRepository) {
        this.studyRepository = studyRepository;
        this.studyParticipationRepository = studyParticipationRepository;
        this.studyNoticeRepository = studyNoticeRepository;
        this.categoryRepository = categoryRepository;
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

    public StudyAuthorityType checkAuthority(Long studyId, Member member){
        Study study = studyRepository.findById(studyId).orElseThrow(
                () -> new BaseException(BaseResponseStatus.NO_SUCH_STUDY)
        );

        if(!studyParticipationRepository.existsByStudyAndMember(study,member)){
            return StudyAuthorityType.NON_MEMBER;
        }

        if(study.getLeaderId().equals(member.getMember_id())){
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
                .study(study)
                .member(member)
                .build();

        studyParticipationRepository.save(studyParticipation);
    }

    @Transactional
    public void createStudy(StudyReq studyReq, Member member){
        Category category = categoryRepository.findByName(studyReq.getCategoryName())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_SUCH_CATEGORY));


        Study study = Study.builder()
                .leaderId(member.getMember_id())
                .category(category)
                .status(StudyStatus.RECRUITING)
                .name(studyReq.getStudyName())
                .thumbnailUrl(uploadImg(studyReq.getThumbnailUrl(), member.getMember_id()))
                .maxMembers(studyReq.getMaxMembers())
                .currentMembers(1)
                .completedAt(LocalDateTime.now().plusWeeks(studyReq.getTargetWeek()))
                .build();

        studyRepository.save(study);
    }

    private String uploadImg(MultipartFile img, Long memberId) {
        String url = "";

        if (!img.isEmpty()) {
            url = "../testFile/" + memberId;
            try {
                img.transferTo(new File(url));
            } catch (IOException e) {
                log.info("uploadImg Error : ", e);
                throw new BaseException(BaseResponseStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return url;

    }

    public List<Study> getRankedStudies() {
        List<Study> studies = studyRepository.findAll();
        studies.forEach(this::calculateRankScore);
        return studies.stream()
                .sorted((s1, s2) -> Double.compare(s2.getRankScore(), s1.getRankScore()))
                .limit(3)
                .collect(Collectors.toList());
    }

    private void calculateRankScore(Study study) {
        int noticeCount = studyNoticeRepository.countByStudy(study);
        double rankScore = noticeCount
                + study.getCurrentMembers()
                + (study.getTotalProgressRate() * 0.1);
        study.setRankScore(rankScore);

    }
}
