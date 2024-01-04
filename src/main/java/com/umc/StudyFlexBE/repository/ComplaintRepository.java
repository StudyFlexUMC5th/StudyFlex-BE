package com.umc.StudyFlexBE.repository;

import com.umc.StudyFlexBE.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

}

