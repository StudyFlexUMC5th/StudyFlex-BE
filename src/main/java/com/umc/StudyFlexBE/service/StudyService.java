package com.umc.StudyFlexBE.service;

import com.umc.StudyFlexBE.entity.Study;
import com.umc.StudyFlexBE.repository.StudyNoticeRepository;
import com.umc.StudyFlexBE.repository.StudyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudyService {

    private final StudyRepository studyRepository;
    private final StudyNoticeRepository studyNoticeRepository;

    @Autowired
    public StudyService(StudyRepository studyRepository, StudyNoticeRepository studyNoticeRepository) {

        this.studyRepository = studyRepository;
        this.studyNoticeRepository =studyNoticeRepository;
    }

    public List<Study> getLatestStudies() {
        return studyRepository.findTop5ByOrderByCreatedAtDesc();
    }
    public List<Study> getOpenStudies() {
        return studyRepository.findByStatus("모집중"); // 또는 StudyStatus.모집중, enum 사용시
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
