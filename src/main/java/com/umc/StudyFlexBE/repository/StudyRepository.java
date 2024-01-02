package com.umc.StudyFlexBE.repository;

import com.umc.StudyFlexBE.entity.Category;
import com.umc.StudyFlexBE.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface StudyRepository extends JpaRepository<Study, Long> {
    Optional<Study> findByCategory(Category category);

    List<Study> findByStudyNameContaining(String query);

    List<Study> findTop5ByOrderByCreatedAtDesc();

    List<Study> findByStatus(String status);

    boolean existsByName(String name);

}
