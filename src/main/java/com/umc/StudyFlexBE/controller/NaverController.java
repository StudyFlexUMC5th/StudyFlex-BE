package com.umc.StudyFlexBE.controller;

import com.umc.StudyFlexBE.dto.request.NaverDto;
import com.umc.StudyFlexBE.entity.MsgEntity;
import com.umc.StudyFlexBE.service.NaverService;
import com.umc.StudyFlexBE.service.SearchService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RequiredArgsConstructor

@RestController
@RequestMapping("naver")
public class NaverController {

    private final NaverService naverService;


    @RequestMapping()
        public String login(Model model){
        model.addAttribute("naverUrl",naverService.getNaverLogin());
        return "index";

        }
    @GetMapping("/callback")
    public ResponseEntity<MsgEntity> callback (HttpServletRequest request)throws Exception{
        NaverDto naverInfo = naverService.getNaverInfo(request.getParameter("code"));

        return ResponseEntity.ok()
                .body(new MsgEntity("Success",naverInfo));
    }


}
