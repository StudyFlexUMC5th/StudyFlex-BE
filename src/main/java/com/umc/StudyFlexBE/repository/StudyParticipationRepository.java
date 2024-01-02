package com.umc.StudyFlexBE.repository;

import com.umc.StudyFlexBE.entity.StudyParticipation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyParticipationRepository extends JpaRepository<StudyParticipation, Long> {
    boolean existsByStudy_idAndMember_id(Long study_id, Long member_id);
}
