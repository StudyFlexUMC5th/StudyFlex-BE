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

    public List<GetParticipationStudyRes> getParticipationStudies(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new BaseException(BaseResponseStatus.NO_SUCH_EMAIL);
        }
        List<StudyParticipation> studyParticipations = studyParticipationRepository.findByMember(member);
        System.out.println(studyParticipations);

        if (studyParticipations.isEmpty()) {
            // 참여 중인 스터디가 없는 경우
            return List.of(GetParticipationStudyRes.builder()
                    .isParticipation(false)
                    .studyId(-1L)
                    .build());
        } else {
            // 참여 중인 스터디가 있는 경우
            return studyParticipations.stream()
                    .map(studyParticipation -> GetParticipationStudyRes.builder()
                            .isParticipation(true)
                            .studyId(studyParticipation.getStudy().getId())
                            .build())
                    .collect(Collectors.toList());
        }
    }

//
//    public GetParticipationStudyRes getParticipationStudy(String email) {
//        Member member = memberRepository.findByEmail(email);
//        if (member == null) {
//            throw new BaseException(BaseResponseStatus.NO_SUCH_EMAIL);
//        }
//
//        List<StudyParticipation> studyParticipation = studyParticipationRepository.findByMember(member);
//
//        if(studyParticipation == null){
//            return GetParticipationStudyRes.builder()
//                    .isParticipation(true)
//                    .studyId(studyParticipation.getStudy().getId())
//                    .build();
//        }
//
//        return GetParticipationStudyRes.builder()
//                .isParticipation(false)
//                .studyId(-1L)
//                .build();
//
//    }

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
