package com.umc.StudyFlexBE.repository;

import com.umc.StudyFlexBE.entity.Completed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletedRepository extends JpaRepository<Completed, Long> {
}
