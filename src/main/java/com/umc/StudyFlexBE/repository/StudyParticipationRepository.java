package com.umc.StudyFlexBE.repository;

import com.umc.StudyFlexBE.entity.Member;
import com.umc.StudyFlexBE.entity.Study;
import com.umc.StudyFlexBE.entity.StudyParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyParticipationRepository extends JpaRepository<StudyParticipation, Long> {
    boolean existsByStudyAndMember(Study study, Member member);
}
