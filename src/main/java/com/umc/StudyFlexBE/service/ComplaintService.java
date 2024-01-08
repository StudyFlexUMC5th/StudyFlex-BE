package com.umc.StudyFlexBE.service;

import com.umc.StudyFlexBE.dto.request.ComplaintRequestDto;
import com.umc.StudyFlexBE.dto.response.BaseException;
import com.umc.StudyFlexBE.dto.response.BaseResponseStatus;
import com.umc.StudyFlexBE.dto.response.ComplaintResponseDto;
import com.umc.StudyFlexBE.entity.Complaint;
import com.umc.StudyFlexBE.entity.ComplaintCategory;
import com.umc.StudyFlexBE.entity.Member;
import com.umc.StudyFlexBE.entity.Study;
import com.umc.StudyFlexBE.repository.ComplaintRepository;
import com.umc.StudyFlexBE.repository.MemberRepository;
import com.umc.StudyFlexBE.repository.StudyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComplaintService {
    private final ComplaintRepository complaintRepository;
    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;

    @Autowired
    public ComplaintService(ComplaintRepository complaintRepository, MemberRepository memberRepository, StudyRepository studyRepository){
        this.complaintRepository = complaintRepository;
        this.memberRepository = memberRepository;
        this.studyRepository = studyRepository;
    }

    public ComplaintResponseDto postComplaint(String email, Long studyId, ComplaintRequestDto request) {
        Member complaintMember = memberRepository.findByEmail(email);
        if (complaintMember == null) {
            throw new BaseException(BaseResponseStatus.NO_SUCH_EMAIL);
        }


        // studyId를 사용하여 리더의 ID를 조회
        Long leaderId = studyRepository.findById(studyId)
                .map(Study::getLeaderId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.STUDY_NOT_FOUND));

        // 리더의 ID를 사용하여 Member 엔터티를 조회
        Member complaintedMember = memberRepository.findById(leaderId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.LEADER_NOT_FOUND));

        Complaint complaint = new Complaint();
        complaint.setComplaint_member(complaintMember);
        complaint.setComplainted_member(complaintedMember);
        complaint.setComplaint_category(ComplaintCategory.valueOf(request.getComplaintCategory()));
        complaint.setContent(request.getContent());

        complaintRepository.save(complaint);

        return new ComplaintResponseDto(complaint.getComplaint_category().name(), complaint.getContent());
    }

}

