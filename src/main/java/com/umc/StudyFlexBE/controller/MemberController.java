package com.umc.StudyFlexBE.controller;


import com.umc.StudyFlexBE.dto.request.LoginDto;
import com.umc.StudyFlexBE.dto.response.BaseException;
import com.umc.StudyFlexBE.dto.response.BaseResponse;
import com.umc.StudyFlexBE.dto.response.BaseResponseStatus;
import com.umc.StudyFlexBE.dto.request.SignUpDto;
import com.umc.StudyFlexBE.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("app/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


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

    @GetMapping("testauth")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public BaseResponse<?> test() {

        return new BaseResponse<String>(BaseResponseStatus.SUCCESS, "굿");
    }
}
