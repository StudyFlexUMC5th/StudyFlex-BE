package com.umc.StudyFlexBE.repository;

import com.umc.StudyFlexBE.entity.Progress;
import com.umc.StudyFlexBE.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgressRepository extends JpaRepository<Progress,Long> {
    Optional<Progress> findByWeekAndStudy(int week, Study study);
    List<Progress> findAllByStudy(Study study);
}
