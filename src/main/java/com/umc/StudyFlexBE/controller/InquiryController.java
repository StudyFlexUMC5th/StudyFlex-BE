package com.umc.StudyFlexBE.controller;

import com.umc.StudyFlexBE.dto.request.Inquiry.InquiryAnswerRequestDto;
import com.umc.StudyFlexBE.dto.request.Inquiry.InquiryUploadRequestDto;
import com.umc.StudyFlexBE.dto.response.BaseResponse;
import com.umc.StudyFlexBE.dto.response.BaseResponseStatus;
import com.umc.StudyFlexBE.dto.response.Inquiry.InquiryAnswerResponseDto;
import com.umc.StudyFlexBE.dto.response.Inquiry.InquiryListResponseDto;
import com.umc.StudyFlexBE.dto.response.Inquiry.InquiryResponseDto;
import com.umc.StudyFlexBE.dto.response.Inquiry.InquiryUploadResponseDto;
import com.umc.StudyFlexBE.entity.Inquiry;
import com.umc.StudyFlexBE.service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/inquiries")
public class InquiryController {
    private final InquiryService inquiryService;

    @Autowired
    public InquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    @PostMapping
    public ResponseEntity<BaseResponse<InquiryUploadResponseDto>> postInquiry(
            @RequestBody InquiryUploadRequestDto request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Inquiry inquiry = inquiryService.createInquiry(email, request);
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

    @GetMapping("/search")
    public ResponseEntity<BaseResponse<InquiryListResponseDto>> searchInquiries(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int itemSize) {
        InquiryListResponseDto inquiryListResponse = inquiryService.searchInquiries(query, page, itemSize);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, inquiryListResponse));
    }

    @PostMapping("/{inquiryId}/answer")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<BaseResponse<InquiryAnswerResponseDto>> postAnswer(
            @PathVariable Long inquiryId,
            @RequestBody InquiryAnswerRequestDto request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            InquiryAnswerResponseDto inquiryAnswerResponse = inquiryService.postAnswer(inquiryId, request, email);
            return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, inquiryAnswerResponse));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new BaseResponse<>(BaseResponseStatus.BAD_REQUEST));
        }
    }
}

