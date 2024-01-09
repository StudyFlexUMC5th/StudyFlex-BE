package com.umc.StudyFlexBE.controller;

import com.umc.StudyFlexBE.dto.request.StudyFlexNoticeUploadDto;
import com.umc.StudyFlexBE.dto.response.*;
import com.umc.StudyFlexBE.dto.response.StudyFlexNotice.StudyFlexNoticeListResponseDto;
import com.umc.StudyFlexBE.dto.response.StudyFlexNotice.StudyFlexNoticeResponseDto;
import com.umc.StudyFlexBE.entity.Notice;
import com.umc.StudyFlexBE.service.StudyFlexNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/notices")
public class StudyFlexNoticeController {
    private final StudyFlexNoticeService StudyFlexNoticeService;

    @Autowired
    public StudyFlexNoticeController(StudyFlexNoticeService StudyFlexNoticeService) {
        this.StudyFlexNoticeService = StudyFlexNoticeService;
    }

    @PostMapping
//    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<BaseResponse<String>> createNotice(@RequestBody StudyFlexNoticeUploadDto request) {
        try {
            Notice notice = StudyFlexNoticeService.createNotice(request);
            return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "공지사항이 등록되었습니다."));
        } catch (BaseException e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getStatus()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new BaseResponse<>(BaseResponseStatus.BAD_REQUEST));
        }
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<BaseResponse<StudyFlexNoticeResponseDto>> getNotice(@PathVariable Long noticeId) {
        try {
            StudyFlexNoticeResponseDto noticeResponse = StudyFlexNoticeService.getNotice(noticeId);
            return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, noticeResponse));
        } catch (BaseException e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getStatus()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new BaseResponse<>(BaseResponseStatus.BAD_REQUEST));
        }
    }

    @GetMapping
    public ResponseEntity<BaseResponse<StudyFlexNoticeListResponseDto>> getNotices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int itemSize) {
        try {
            StudyFlexNoticeListResponseDto noticeListResponse = StudyFlexNoticeService.getNoticeList(page, itemSize);
            return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, noticeListResponse));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new BaseResponse<>(BaseResponseStatus.BAD_REQUEST));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<BaseResponse<StudyFlexNoticeListResponseDto>> searchNotices(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int itemSize) {
        try {
            StudyFlexNoticeListResponseDto noticeListResponse = query == null
                    ? StudyFlexNoticeService.getNoticeList(page, itemSize)
                    : StudyFlexNoticeService.searchNotices(query, page, itemSize);

            return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, noticeListResponse));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new BaseResponse<>(BaseResponseStatus.BAD_REQUEST));
        }
    }

}

