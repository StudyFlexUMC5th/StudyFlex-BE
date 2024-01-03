package com.umc.StudyFlexBE.controller;


import com.umc.StudyFlexBE.dto.request.CheckAuthCodeDto;
import com.umc.StudyFlexBE.dto.request.LoginDto;
import com.umc.StudyFlexBE.dto.request.SendAuthCodeDto;
import com.umc.StudyFlexBE.dto.request.SignUpDto;
import com.umc.StudyFlexBE.dto.request.SignUpOAuthDto;
import com.umc.StudyFlexBE.dto.response.BaseException;
import com.umc.StudyFlexBE.dto.response.BaseResponse;
import com.umc.StudyFlexBE.dto.response.BaseResponseStatus;
import com.umc.StudyFlexBE.entity.KaKaoOAuthToken;
import com.umc.StudyFlexBE.service.MemberService;
import com.univcert.api.UnivCert;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("app/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final RestTemplate restTemplate;
    @Value("${mail.api.key}")
    private String mail_api_key;
    @GetMapping("/checkEmail/{email}")
    public BaseResponse<?> checkEmail(@PathVariable String email) {
        try {
            Boolean notDuplicate = memberService.checkEmail(email);
            if (notDuplicate.equals(true)) {
                return new BaseResponse<>(BaseResponseStatus.SUCCESS, "이메일 사용 가능");
            }
            return new BaseResponse<>(BaseResponseStatus.DUPLICATE_EMAIL, "이메일 사용 불가능");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }


    @PostMapping("/signUp")
    public BaseResponse<?> signUp(@RequestBody @Valid SignUpDto signUpDto) {
        try {
            memberService.signUp(signUpDto);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, "회원가입 성공");
        } catch (BaseException e) {
            return new BaseResponse(e.getStatus());
        }
    }

    @PostMapping("/login")
    public BaseResponse<?> login(@RequestBody LoginDto loginDto) {
        try {

            String token = memberService.login(loginDto);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, token);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }


    @GetMapping("/kakao/callback")
    public BaseResponse<?> kakaoCallback(String code) {
        try {
            //code 이용 하여 oAuthAccessToken 얻어옴
            KaKaoOAuthToken kaKaoOAuthToken = memberService.getKakaoToken(code);
            //oAuthAccessToken 으로 nickname 가져옴
            String nickname = memberService.getOAuthInfo(kaKaoOAuthToken);
            // 해당 nickname 으로 된 계정이 있는지 확인
            Boolean notDuplicate = memberService.checkEmail(nickname);
            // 없다면 회원가입 후 로그인
            if (notDuplicate.equals(true)) {
                SignUpOAuthDto signUpOAuthDto = new SignUpOAuthDto();
                signUpOAuthDto.setName(nickname);
                signUpOAuthDto.setEmail(nickname);
                memberService.signUpOAUth(signUpOAuthDto);
                LoginDto loginDto = new LoginDto();
                loginDto.setEmail(nickname);
                loginDto.setPassword("12345");
                String token = memberService.login(loginDto);
                return new BaseResponse<>(BaseResponseStatus.SUCCESS, token);
            }
            // 있다면 로그인
            LoginDto loginDto = new LoginDto();
            loginDto.setEmail(nickname);
            loginDto.setPassword("12345");
            String token = memberService.login(loginDto);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, token);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }


    @PostMapping("/sendAuthCode")
    public BaseResponse<?> senAuthCode(@RequestBody SendAuthCodeDto sendAuthCodeDto)  {
        try {
            Map<String, Object> result = UnivCert.certify(mail_api_key, sendAuthCodeDto.getEmail(), sendAuthCodeDto.getUnivName(), true);
            if (result.isEmpty()) {
                return new BaseResponse<>(BaseResponseStatus.SEND_EMAIL_FAILED);
            }
            return new BaseResponse<String>(BaseResponseStatus.SUCCESS, "인증 코드 발송 완료.");
        } catch (Exception e){
            return new BaseResponse<>(BaseResponseStatus.SEND_EMAIL_FAILED);
        }
    }

    @PostMapping("/checkAuthCode")
    public BaseResponse<?> checkAuthCode(@RequestBody CheckAuthCodeDto checkAuthCodeDto) throws IOException {
        Map<String, Object> result =UnivCert.certifyCode(mail_api_key,checkAuthCodeDto.getWebEmail(),checkAuthCodeDto.getUnivName(), checkAuthCodeDto.getCode());
        if (result.isEmpty()) {
            return new BaseResponse<>(BaseResponseStatus.WEB_MAIL_CODE_FAILED);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        memberService.certifyWebMail(email, checkAuthCodeDto.getUnivName(), checkAuthCodeDto.getWebEmail());
        return new BaseResponse<String>(BaseResponseStatus.SUCCESS, "인증 코드 확인 완료.");
    }

    @GetMapping("testauth")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public BaseResponse<?> test() {

        return new BaseResponse<String>(BaseResponseStatus.SUCCESS, "굿");
    }
}
