package com.umc.StudyFlexBE.controller;

import com.umc.StudyFlexBE.dto.response.BaseResponse;
import com.umc.StudyFlexBE.dto.response.BaseResponseStatus;
import com.umc.StudyFlexBE.entity.Study;
import com.umc.StudyFlexBE.service.SearchService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/app/search")
public class SearchController {
    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {

        this.searchService = searchService;
    }

    @GetMapping()
    public ResponseEntity<BaseResponse<?>> searchStudies(@RequestParam String query) {
        try {
            Study searchResult = searchService.searchStudies(query);
            if (searchResult == null) {
                return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.NO_SUCH_STUDY));
            } else {
                Map<String, Object> studyData = new HashMap<>();
                studyData.put("studyId", searchResult.getId());
                studyData.put("studyName", searchResult.getName());
                return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, studyData));
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new BaseResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    }
