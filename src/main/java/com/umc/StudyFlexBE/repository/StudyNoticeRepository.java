package com.umc.StudyFlexBE.repository;

import com.umc.StudyFlexBE.entity.Study;
import com.umc.StudyFlexBE.entity.StudyNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyNoticeRepository extends JpaRepository<StudyNotice, Long> {
    int countByStudy(Study study);
    List<StudyNotice> findAllByStudy(Study study);

}
