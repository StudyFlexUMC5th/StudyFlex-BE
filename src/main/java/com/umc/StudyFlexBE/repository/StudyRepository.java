package com.umc.StudyFlexBE.repository;

import com.umc.StudyFlexBE.entity.Category;
import com.umc.StudyFlexBE.entity.Study;
import com.umc.StudyFlexBE.entity.StudyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface StudyRepository extends JpaRepository<Study, Long> {
    @Override
    Optional<Study> findById(Long aLong);

    Optional<Study> findByCategory(Category category);

    List<Study> findByNameContaining(String query);

    List<Study> findTop5ByOrderByCreatedAtDesc();

    List<Study> findByStatus(StudyStatus studyStatus);

    boolean existsByName(String name);



}
