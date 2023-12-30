package com.umc.StudyFlexBE.service;


import static com.umc.StudyFlexBE.entity.MemberType.*;
import static com.umc.StudyFlexBE.entity.Role.*;

import com.umc.StudyFlexBE.dto.request.SignUpDto;
import com.umc.StudyFlexBE.dto.response.BaseException;
import com.umc.StudyFlexBE.dto.response.BaseResponseStatus;
import com.umc.StudyFlexBE.entity.Member;
import com.umc.StudyFlexBE.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    public boolean checkEmail(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            return true;
        }
        return false;
    }

    @Transactional
    public void signUp(SignUpDto signUpDto) {

        Member member = new Member();
        member.setMember_type(general);
        member.setRole(ROLE_USER);
        member.setEmail(signUpDto.getEmail());
        member.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        memberRepository.save(member);
    }


}
