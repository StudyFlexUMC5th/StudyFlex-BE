package com.umc.StudyFlexBE.service;


import static com.umc.StudyFlexBE.entity.MemberType.*;
import static com.umc.StudyFlexBE.entity.Role.*;

import com.umc.StudyFlexBE.dto.request.LoginDto;
import com.umc.StudyFlexBE.dto.request.SignUpDto;
import com.umc.StudyFlexBE.dto.response.BaseException;
import com.umc.StudyFlexBE.dto.response.BaseResponseStatus;
import com.umc.StudyFlexBE.entity.Member;
import com.umc.StudyFlexBE.jwt.JwtTokenProvider;
import com.umc.StudyFlexBE.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

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


    public String login(LoginDto loginDto) {
        Member member = memberRepository.findByEmail(loginDto.getEmail());
        if (member == null) {
            throw new BaseException(BaseResponseStatus.NO_SUCH_EMAIL);
        }
        if (!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
            throw new BaseException(BaseResponseStatus.WRONG_PASSWORD);
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(), loginDto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.createToken(authentication);
        String token = "Bearer " + jwt;
        return token;
    }
}
