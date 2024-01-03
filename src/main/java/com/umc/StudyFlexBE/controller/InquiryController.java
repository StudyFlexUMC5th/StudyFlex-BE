package com.umc.StudyFlexBE.controller;

import com.umc.StudyFlexBE.dto.request.Inquiry.InquiryAnswerRequestDto;
import com.umc.StudyFlexBE.dto.request.Inquiry.InquiryUploadRequestDto;
import com.umc.StudyFlexBE.dto.response.*;
import com.umc.StudyFlexBE.dto.response.Inquiry.InquiryAnswerResponseDto;
import com.umc.StudyFlexBE.dto.response.Inquiry.InquiryListResponseDto;
import com.umc.StudyFlexBE.dto.response.Inquiry.InquiryResponseDto;
import com.umc.StudyFlexBE.dto.response.Inquiry.InquiryUploadResponseDto;
import com.umc.StudyFlexBE.entity.Inquiry;
import com.umc.StudyFlexBE.security.CustomUserDetails;
import com.umc.StudyFlexBE.service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/inquiry")
public class InquiryController {
    private final InquiryService inquiryService;

    @Autowired
    public InquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    @PostMapping("/postNotice")
    public ResponseEntity<BaseResponse<InquiryUploadResponseDto>> postInquiry(
            @RequestBody InquiryUploadRequestDto request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long memberId = userDetails.getMemberId();

            Inquiry inquiry = inquiryService.createInquiry(memberId, request);
            return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, new InquiryUploadResponseDto(inquiry.getId())));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new BaseResponse<>(BaseResponseStatus.BAD_REQUEST));
        }
    }

    @GetMapping
    public ResponseEntity<BaseResponse<InquiryListResponseDto>> getInquiries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int itemSize) {
        try {
            InquiryListResponseDto inquiryListResponse = inquiryService.getInquiryList(page, itemSize);
            return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, inquiryListResponse));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new BaseResponse<>(BaseResponseStatus.BAD_REQUEST));
        }
    }

    @PostMapping("/{inquiryId}")
    public ResponseEntity<BaseResponse<InquiryResponseDto>> getInquiryDetail(@PathVariable Long inquiryId) {
        try {
            InquiryResponseDto inquiryDetail = inquiryService.getInquiryDetail(inquiryId);
            return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, inquiryDetail));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new BaseResponse<>(BaseResponseStatus.BAD_REQUEST));
        }
    }

    @GetMapping
    public ResponseEntity<BaseResponse<InquiryListResponseDto>> searchInquiries(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int itemSize) {
        InquiryListResponseDto inquiryListResponse = inquiryService.searchInquiries(query, page, itemSize);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, inquiryListResponse));
    }

    @PostMapping("/{inquiryId}/answer")
    public ResponseEntity<BaseResponse<InquiryAnswerResponseDto>> postAnswer(
            @PathVariable Long inquiryId,
            @RequestBody InquiryAnswerRequestDto request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long memberId = userDetails.getMemberId();

            InquiryAnswerResponseDto inquiryAnswerResponse = inquiryService.postAnswer(inquiryId, request, memberId);
            return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, inquiryAnswerResponse));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new BaseResponse<>(BaseResponseStatus.BAD_REQUEST));
        }
    }
}

