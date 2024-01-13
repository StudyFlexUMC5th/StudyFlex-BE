package com.umc.StudyFlexBE.service;

import com.umc.StudyFlexBE.dto.response.BaseException;
import com.umc.StudyFlexBE.dto.response.BaseResponseStatus;
import com.umc.StudyFlexBE.dto.response.GetParticipationStudyRes;
import com.umc.StudyFlexBE.entity.Member;
import com.umc.StudyFlexBE.entity.StudyParticipation;
import com.umc.StudyFlexBE.repository.MemberRepository;
import com.umc.StudyFlexBE.repository.StudyParticipationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
