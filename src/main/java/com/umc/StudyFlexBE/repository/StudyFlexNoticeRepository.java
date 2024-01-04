package com.umc.StudyFlexBE.repository;

import com.umc.StudyFlexBE.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface StudyFlexNoticeRepository extends JpaRepository<Notice, Long> {
//    List<Notice> findByMemberId(Long memberId);
//
//    List<Notice> findByTitleContaining(String title);

    @Query("SELECT n FROM Notice n WHERE n.title LIKE %:searchTerm% OR n.content LIKE %:searchTerm%")
    Page<Notice> findByTitleOrContentContaining(@Param("searchTerm") String searchTerm, Pageable pageable);
}
