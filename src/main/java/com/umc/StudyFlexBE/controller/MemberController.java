package com.umc.StudyFlexBE.controller;


import com.umc.StudyFlexBE.dto.response.BaseException;
import com.umc.StudyFlexBE.dto.response.BaseResponse;
import com.umc.StudyFlexBE.dto.response.BaseResponseStatus;
import com.umc.StudyFlexBE.dto.request.SignUpDto;
import com.umc.StudyFlexBE.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("app/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signUp")
    public BaseResponse<?> signUp(@RequestBody @Valid SignUpDto signUpDto) {
        try{
            memberService.signUp(signUpDto);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS,"회원가입 성공");
        }catch (BaseException e){
            return new BaseResponse(e.getStatus());
        }
    }

}
