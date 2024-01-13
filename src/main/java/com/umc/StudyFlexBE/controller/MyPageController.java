package com.umc.StudyFlexBE.controller;

import com.umc.StudyFlexBE.dto.response.*;
import com.umc.StudyFlexBE.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/myPage")
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping
    public BaseResponse<?> getMyPage(){
        try {
            String email = getEmail();
            MyPageRes res = myPageService.getMyPage(email);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, res);
        }catch (BaseException e) {
            if(e.getStatus().equals(BaseResponseStatus.NO_SUCH_EMAIL)) {
                return new BaseResponse<>(e.getStatus());
            }else{
                return new BaseResponse<>(BaseResponseStatus.GET_MY_PAGE_FAILED);
            }
        }

    }

    @GetMapping("/myStudy")
    public BaseResponse<?> getParticipationStudy(){
        try {
            String email = getEmail();
            GetParticipationStudyRes res = myPageService.getParticipationStudy(email);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS,res);
        } catch (BaseException e) {
            if(e.getStatus().equals(BaseResponseStatus.NO_SUCH_EMAIL)) {
                return new BaseResponse<>(e.getStatus());
            }else{
                return new BaseResponse<>(BaseResponseStatus.GET_MY_STUDY_FAILED);
            }
        }
    }

    private String getEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
