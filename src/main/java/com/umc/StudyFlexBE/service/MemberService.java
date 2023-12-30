package com.umc.StudyFlexBE.service;


import static com.umc.StudyFlexBE.entity.MemberType.*;
import static com.umc.StudyFlexBE.entity.Role.*;

import com.umc.StudyFlexBE.dto.SignUpDto;
import com.umc.StudyFlexBE.entity.Member;
import com.umc.StudyFlexBE.entity.MemberType;
import com.umc.StudyFlexBE.entity.Role;
import com.umc.StudyFlexBE.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public void signUp(SignUpDto signUpDto) {
        Member member = new Member();
        member.setMember_type(general);
        member.setRole(ROLE_USER);
        member.setEmail(signUpDto.getEmail());
        member.setPassword(signUpDto.getPassword());
        memberRepository.save(member);
    }


}
