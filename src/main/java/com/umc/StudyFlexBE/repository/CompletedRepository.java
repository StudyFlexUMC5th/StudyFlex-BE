package com.umc.StudyFlexBE.repository;

import com.umc.StudyFlexBE.entity.Completed;
import com.umc.StudyFlexBE.entity.Member;
import com.umc.StudyFlexBE.entity.Progress;
import com.umc.StudyFlexBE.entity.StudyParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletedRepository extends JpaRepository<Completed, Long> {
    boolean existsByProgressAndStudyParticipation(Progress progress, StudyParticipation studyParticipation);

    long countByProgress(Progress progress);

}
