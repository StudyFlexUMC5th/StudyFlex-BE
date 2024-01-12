package com.umc.StudyFlexBE.controller;

import com.umc.StudyFlexBE.dto.response.BaseResponse;
import com.umc.StudyFlexBE.dto.response.BaseResponseStatus;
import com.umc.StudyFlexBE.entity.Study;
import com.umc.StudyFlexBE.service.SearchService;
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
    private BaseResponse<?> response;

    @Autowired
    public SearchController(SearchService searchService) {

        this.searchService = searchService;
    }

    @GetMapping()
    public ResponseEntity<BaseResponse<?>> searchStudies(@RequestParam String query) {
        try {
            Study searchResult = searchService.searchStudies(query);
            if (searchResult == null) {
                BaseResponse<?> response = new BaseResponse<>(BaseResponseStatus.NO_SUCH_STUDY);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            } else {
                Map<String, Object> studyData = new HashMap<>();
                studyData.put("studyId", searchResult.getId());
                studyData.put("studyName", searchResult.getName());
                BaseResponse<Map<String, Object>> response = new BaseResponse<>(BaseResponseStatus.SUCCESS, studyData);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            BaseResponse<?> response = new BaseResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
