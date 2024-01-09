package com.umc.StudyFlexBE.service;


import static com.umc.StudyFlexBE.entity.MemberType.general;
import static com.umc.StudyFlexBE.entity.Role.ROLE_CERTIFIED;
import static com.umc.StudyFlexBE.entity.Role.ROLE_USER;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.StudyFlexBE.config.jwt.JwtTokenProvider;
import com.umc.StudyFlexBE.dto.request.LoginDto;
import com.umc.StudyFlexBE.dto.request.SignUpDto;
import com.umc.StudyFlexBE.dto.request.SignUpOAuthDto;
import com.umc.StudyFlexBE.dto.response.BaseException;
import com.umc.StudyFlexBE.dto.response.BaseResponseStatus;
import com.umc.StudyFlexBE.entity.KaKaoOAuthToken;
import com.umc.StudyFlexBE.entity.Member;
import com.umc.StudyFlexBE.entity.OAuthProfile;
import com.umc.StudyFlexBE.repository.MemberRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RestTemplate restTemplate;
    private final JavaMailSender javaMailSender;


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
        member.setEmail(signUpDto.getEmail());
        member.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        member.setName(signUpDto.getName());
        member.setSchool(signUpDto.getSchool());
        member.setRole(ROLE_USER);
        memberRepository.save(member);
    }

    @Transactional
    public void signUpOAUth(SignUpOAuthDto signUpOAuthDto) {
        Member member = new Member();
        member.setMember_type(general);
        member.setEmail(signUpOAuthDto.getEmail());
        member.setName(signUpOAuthDto.getName());
        member.setPassword(passwordEncoder.encode("12345"));
        member.setSchool(signUpOAuthDto.getSchool());
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

        try {
            Authentication authentication = authenticationManagerBuilder.getObject()
                    .authenticate(usernamePasswordAuthenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenProvider.createToken(authentication);
            String token = "Bearer " + jwt;
            return token;

        } catch (Exception e) {
            System.out.println("Authentication failed: " + e.getMessage());
            // Optionally log more details or rethrow the exception
            return null;
        }
    }


    public KaKaoOAuthToken getKakaoToken(String code) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", "a6e75ba812b0214d4f01fdaec0af6ac1");
            params.add("redirect_uri", "http://localhost:8080/app/member/kakao/callback");
            params.add("code", code);
            HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    "https://kauth.kakao.com/oauth/token", // https://{요청할 서버 주소}
                    HttpMethod.POST, // 요청할 방식
                    kakaoTokenRequest, // 요청할 때 보낼 데이터
                    String.class // 요청 시 반환 되는 데이터 타입
            );
            //response objectMapper 로 파싱 하여 oAuthAccessToken 얻어냄
            ObjectMapper objectMapper = new ObjectMapper();
            KaKaoOAuthToken kaKaoOAuthToken = null;
            kaKaoOAuthToken = objectMapper.readValue(response.getBody(), KaKaoOAuthToken.class);
            log.info(kaKaoOAuthToken.getAccess_token().toString());
            return kaKaoOAuthToken;
        } catch (JsonProcessingException e) {
            throw new BaseException(BaseResponseStatus.GET_OAUTH_TOKEN_FAILED);
        }
    }


    public String getOAuthInfo(KaKaoOAuthToken oAuthAccessToken) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + oAuthAccessToken.getAccess_token());
            headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    "https://kapi.kakao.com/v2/user/me", // https://{요청할 서버 주소}
                    HttpMethod.POST, // 요청할 방식
                    kakaoProfileRequest, // 요청할 때 보낼 데이터
                    String.class // 요청 시 반환 되는 데이터 타입
            );
            log.info(response.getBody());
            ObjectMapper objectMapper = new ObjectMapper();
            OAuthProfile oAuthProfile = null;
            oAuthProfile = objectMapper.readValue(response.getBody(), OAuthProfile.class);
            log.info(oAuthProfile.getProperties().getNickname());
            return oAuthProfile.getProperties().getNickname();
        } catch (JsonProcessingException e) {
            throw new BaseException(BaseResponseStatus.GET_OAUTH_INFO_FAILED);
        }


    }


    @Transactional
    public void certifyWebMail(String email, String school, String webEmail) {
        Member member = memberRepository.findByEmail(email);
        member.setSchool(school);
        member.setWeb_email(webEmail);
        member.setRole(ROLE_CERTIFIED);
        memberRepository.save(member);
    }

    @Transactional
    public void deleteMember(String email) {

        Long memberId = memberRepository.deleteByEmail(email);

    }


    @Transactional
    public void sendPasswordMail(String email, String password) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "utf-8");

        try {
            // 1. 메일 수신자 설정
            messageHelper.setTo(email);
            // 2. 메일 제목 설정
            messageHelper.setSubject("[StudyFlex] Your New Password");
            // 3. 메일 내용 설정
            String mailContent = "[StudyFlex] Your new password is: " + password;
            messageHelper.setText(mailContent, true);
            // 4. 메일 전송
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.MAIL_SEND_FAILED);
        }

        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new BaseException(BaseResponseStatus.NO_SUCH_EMAIL);
        }
        member.setPassword(passwordEncoder.encode(password));
        memberRepository.save(member);


    }


    @Transactional
    public void changeEmail(String email, String newEmail) {

        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new BaseException(BaseResponseStatus.NO_SUCH_EMAIL);
        }
        if(checkEmail(newEmail) == false){
            throw new BaseException(BaseResponseStatus.CHANGE_EMAIL_FAILED);
        }

        member.setEmail(newEmail);
        memberRepository.save(member);

    }
}