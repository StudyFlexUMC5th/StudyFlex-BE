package com.umc.StudyFlexBE.repository;

import com.umc.StudyFlexBE.entity.Progress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressRepository extends JpaRepository<Progress,Long> {
}
