package com.umc.StudyFlexBE.service;

import com.umc.StudyFlexBE.dto.request.StudyNoticeReq;
import com.umc.StudyFlexBE.dto.request.StudyReq;
import com.umc.StudyFlexBE.dto.response.*;
import com.umc.StudyFlexBE.entity.*;
import com.umc.StudyFlexBE.repository.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StudyService {

    private final StudyRepository studyRepository;
    private final StudyParticipationRepository studyParticipationRepository;
    private final StudyNoticeRepository studyNoticeRepository;
    private final CategoryRepository categoryRepository;
    private final ProgressRepository progressRepository;
    private final CompletedRepository completedRepository;


    @Autowired
    public StudyService(
            StudyRepository studyRepository,
            StudyParticipationRepository studyParticipationRepository,
            StudyNoticeRepository studyNoticeRepository,
            CategoryRepository categoryRepository,
            ProgressRepository progressRepository,
            CompletedRepository completedRepository
    ) {
        this.studyRepository = studyRepository;
        this.studyParticipationRepository = studyParticipationRepository;
        this.studyNoticeRepository = studyNoticeRepository;
        this.categoryRepository = categoryRepository;
        this.progressRepository = progressRepository;
        this.completedRepository = completedRepository;
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

    @Transactional
    public void participation(Long studyId, Member member){

        //스터디 참여 테이블에 반영
        Study study = studyRepository.findById(studyId).orElseThrow(
                () -> new BaseException(BaseResponseStatus.NO_SUCH_STUDY)
        );

        if(study.getStatus().equals(StudyStatus.COMPLETED)){
            throw new BaseException(BaseResponseStatus.FULL_STUDY_MEMBER);
        }
        StudyParticipation studyParticipation = StudyParticipation.builder()
                .study(study)
                .member(member)
                .build();

        studyParticipationRepository.save(studyParticipation);

        //스터디 현제 인원 변경
        study.participationStudy();
    }

    @Transactional
    public void createStudy(StudyReq studyReq, Member member){

        //스터디 생성 로직
        Category category = categoryRepository.findByName(studyReq.getCategoryName())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_SUCH_CATEGORY));


        Study study = Study.builder()
                .leaderId(member.getMember_id())
                .category(category)
                .status(StudyStatus.RECRUITING)
                .name(studyReq.getStudyName())
                .thumbnailUrl(uploadImg(studyReq.getThumbnailUrl(), member.getMember_id()))
                .maxMembers(studyReq.getMaxMembers())
                .targetWeek(studyReq.getTargetWeek())
                .currentMembers(1)
                .completedAt(LocalDateTime.now().plusWeeks(studyReq.getTargetWeek()))
                .build();

        studyRepository.save(study);

        // 스터디 주차별 정보 생성 로직
        List<Progress> progressesList = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for(int i = 0; i < studyReq.getTargetWeek(); i++){
            progressesList.add(
                    Progress.builder()
                            .startDate(today.plusWeeks(i))
                            .week(i+1)
                            .study(study)
                            .build()
            );
        }

        progressRepository.saveAll(progressesList);
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

    public void postStudyNotice(Long studyId, Member member, StudyNoticeReq studyNoticeReq){
        if(!checkAuthority(studyId,member).equals(StudyAuthorityType.LEADER)){
            throw new BaseException(BaseResponseStatus.NO_AUTHORITY);
        }

        Study study = studyRepository.findById(studyId).orElseThrow(
                () -> new BaseException(BaseResponseStatus.NO_SUCH_STUDY)
        );

        StudyNotice studyNotice = StudyNotice.builder()
                .title(studyNoticeReq.getTitle())
                .content(studyNoticeReq.getContent())
                .study(study)
                .build();

        studyNoticeRepository.save(studyNotice);
    }

    public StudyNoticeRes getStudyNotice(Long studyId, Long noticeId, Member member){
        if(checkAuthority(studyId,member).equals(StudyAuthorityType.NON_MEMBER)){
            throw new BaseException(BaseResponseStatus.NO_AUTHORITY);
        }

        StudyNotice studyNotice = studyNoticeRepository.findById(noticeId).orElseThrow(
                () -> new BaseException(BaseResponseStatus.NO_SUCH_STUDY_NOTICE)
        );

        return StudyNoticeRes.builder()
                .title(studyNotice.getTitle())
                .content(studyNotice.getContent())
                .createAt(studyNotice.getCreatedAt())
                .build();
    }

    public void deleteStudyNotice(Long studyId, Long noticeId, Member member) {
        if(!checkAuthority(studyId,member).equals(StudyAuthorityType.LEADER)){
            throw new BaseException(BaseResponseStatus.NO_AUTHORITY);
        }

        studyNoticeRepository.deleteById(noticeId);
    }

    public StudyNoticesInfoRes getStudyNotices(Long studyId, Member member) {
        if(checkAuthority(studyId,member).equals(StudyAuthorityType.NON_MEMBER)){
            throw new BaseException(BaseResponseStatus.NO_AUTHORITY);
        }

        Study study = studyRepository.findById(studyId).orElseThrow(
                () -> new BaseException(BaseResponseStatus.NO_SUCH_STUDY)
        );

        List<StudyNoticesRes> studyNoticesRes = studyNoticeRepository.findAllByStudy(study)
                .stream()
                .map(studyNotice ->
                        StudyNoticesRes.builder()
                                .title(studyNotice.getTitle())
                                .createAt(studyNotice.getCreatedAt())
                                .build()
                ).collect(Collectors.toList());

        return StudyNoticesInfoRes.builder()
                .notices(studyNoticesRes)
                .itemSize(studyNoticesRes.size())
                .build();
    }

    @Transactional
    public ProgressReq checkCompletedStudyWeek(long studyId, int week, Member member) {
        Study study = studyRepository.findById(studyId).orElseThrow(
                () -> new BaseException(BaseResponseStatus.NO_SUCH_STUDY)
        );

        StudyParticipation studyParticipation = studyParticipationRepository.findByStudyAndMember(study, member).orElseThrow(
                () -> new BaseException(BaseResponseStatus.NO_STUDY_PARTICIPANT)
        );

        Progress progress = progressRepository.findByWeekAndStudy(week, study).orElseThrow(
                () -> new BaseException(BaseResponseStatus.NO_SUCH_WEEK)
        );



        completedRepository.save(
                Completed.builder()
                        .studyParticipation(studyParticipation)
                        .progress(progress)
                        .build()
        );

        progress.addCompletedNumber();

        double rate = (progress.getCompletedNumber()*1.0)/study.getCurrentMembers();

        return ProgressReq.builder()
                .completed(true)
                .participant_rate(rate)
                .start_date(progress.getStartDate())
                .build();
    }
}
