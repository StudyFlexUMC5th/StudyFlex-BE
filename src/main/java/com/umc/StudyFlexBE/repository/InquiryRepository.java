package com.umc.StudyFlexBE.repository;


import com.umc.StudyFlexBE.entity.Inquiry;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    @Query("SELECT i FROM Inquiry i WHERE i.title LIKE %:searchTerm% OR i.content LIKE %:searchTerm%")
    Page<Inquiry> findByTitleOrContentContaining(String searchTerm, Pageable pageable);

}

