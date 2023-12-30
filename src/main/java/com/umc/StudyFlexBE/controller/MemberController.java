package com.umc.StudyFlexBE.controller;


import com.umc.StudyFlexBE.dto.SignUpDto;
import com.umc.StudyFlexBE.service.MemberService;
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
    public String signUp(@RequestBody SignUpDto signUpDto) {
        try{
            memberService.signUp(signUpDto);

        }catch (Exception e){
            return e.getMessage();
        }

        return "회원가입 성공";
    }



}
