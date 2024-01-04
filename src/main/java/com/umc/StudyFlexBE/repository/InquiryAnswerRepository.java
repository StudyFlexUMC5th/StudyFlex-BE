package com.umc.StudyFlexBE.repository;

import com.umc.StudyFlexBE.entity.Inquiry;
import com.umc.StudyFlexBE.entity.InquiryAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InquiryAnswerRepository extends JpaRepository<InquiryAnswer, Long> {
    Optional<InquiryAnswer> findByInquiry(Inquiry inquiry); // 메소드 이름 변경
}


