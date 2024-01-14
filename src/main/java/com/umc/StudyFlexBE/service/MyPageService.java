package com.umc.StudyFlexBE.service;

import com.umc.StudyFlexBE.dto.response.*;
import com.umc.StudyFlexBE.entity.Member;
import com.umc.StudyFlexBE.entity.StudyParticipation;
import com.umc.StudyFlexBE.repository.MemberRepository;
import com.umc.StudyFlexBE.repository.StudyParticipationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final StudyParticipationRepository studyParticipationRepository;
    private final MemberRepository memberRepository;

    public MyStudyListRes getParticipationStudies(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new BaseException(BaseResponseStatus.NO_SUCH_EMAIL);
        }
        List<StudyParticipation> studyParticipations = studyParticipationRepository.findAllByMember(member);
        System.out.println(studyParticipations);

        List<MyStudyRes> myStudyList = studyParticipations.stream()
                .map(studyParticipation -> MyStudyRes.builder()
                        .studyId(studyParticipation.getStudy().getId())
                        .name(studyParticipation.getStudy().getName())
                        .thumbnailUrl(studyParticipation.getStudy().getThumbnailUrl())
                        .build())
                .collect(Collectors.toList());

        return MyStudyListRes.builder()
                .count(myStudyList.size())
                .myStudyList(myStudyList)
                .build();
    }

    public MyPageRes getMyPage(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new BaseException(BaseResponseStatus.NO_SUCH_EMAIL);
        }

        boolean isSchoolCertification = member.getSchool() != null;

        return MyPageRes.builder()
                .email(member.getEmail())
                .name(member.getName())
                .schoolName(member.getSchool())
                .isSchoolCertification(isSchoolCertification)
                .build();
    }
}
