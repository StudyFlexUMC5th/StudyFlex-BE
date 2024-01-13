package com.umc.StudyFlexBE.controller;

import com.umc.StudyFlexBE.dto.response.BaseException;
import com.umc.StudyFlexBE.dto.response.BaseResponse;
import com.umc.StudyFlexBE.dto.response.BaseResponseStatus;
import com.umc.StudyFlexBE.dto.response.GetParticipationStudyRes;
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

    @GetMapping("/myStudy")
    public BaseResponse<?> getParticipationStudy(){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
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
}
