package com.umc.StudyFlexBE.service;

import com.umc.StudyFlexBE.dto.request.ComplaintRequestDto;
import com.umc.StudyFlexBE.dto.response.ComplaintResponseDto;
import com.umc.StudyFlexBE.entity.Complaint;
import com.umc.StudyFlexBE.entity.ComplaintCategory;
import com.umc.StudyFlexBE.entity.Member;
import com.umc.StudyFlexBE.repository.ComplaintRepository;
import com.umc.StudyFlexBE.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ComplaintService {
    private final ComplaintRepository complaintRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public ComplaintService(ComplaintRepository complaintRepository, MemberRepository memberRepository) {
        this.complaintRepository = complaintRepository;
        this.memberRepository = memberRepository;
    }

    public ComplaintResponseDto postComplaint(Long memberId, ComplaintRequestDto request) {
        Member complaintMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Member complaintedMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Complaint complaint = new Complaint();
        complaint.setComplaint_member(complaintMember);
        complaint.setComplainted_member(complaintedMember);
        complaint.setComplaint_category(ComplaintCategory.valueOf(request.getComplaintCategory()));
        complaint.setContent(request.getContent());

        complaintRepository.save(complaint);

        return new ComplaintResponseDto(complaint.getComplaint_category().name(), complaint.getContent());
    }
}

