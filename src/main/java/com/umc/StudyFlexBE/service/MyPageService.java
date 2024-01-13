package com.umc.StudyFlexBE.service;

import com.umc.StudyFlexBE.dto.response.BaseException;
import com.umc.StudyFlexBE.dto.response.BaseResponseStatus;
import com.umc.StudyFlexBE.dto.response.GetParticipationStudyRes;
import com.umc.StudyFlexBE.dto.response.MyPageRes;
import com.umc.StudyFlexBE.entity.Member;
import com.umc.StudyFlexBE.entity.StudyParticipation;
import com.umc.StudyFlexBE.repository.MemberRepository;
import com.umc.StudyFlexBE.repository.StudyParticipationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final StudyParticipationRepository studyParticipationRepository;
    private final MemberRepository memberRepository;

    public GetParticipationStudyRes getParticipationStudy(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new BaseException(BaseResponseStatus.NO_SUCH_EMAIL);
        }

        StudyParticipation studyParticipation = studyParticipationRepository.findByMember(member).orElse(null);

        if(studyParticipation == null){
            return GetParticipationStudyRes.builder()
                    .isParticipation(true)
                    .studyId(studyParticipation.getStudy().getId())
                    .build();
        }

        return GetParticipationStudyRes.builder()
                .isParticipation(false)
                .studyId(-1L)
                .build();

    }

    public MyPageRes getMyPage(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new BaseException(BaseResponseStatus.NO_SUCH_EMAIL);
        }

        boolean isSchoolCertification = true;

        if(member.getSchool() == null){
            isSchoolCertification = false;
        }

        return MyPageRes.builder()
                .email(member.getEmail())
                .name(member.getName())
                .schoolName(member.getSchool())
                .isSchoolCertification(isSchoolCertification)
                .build();
    }
}
