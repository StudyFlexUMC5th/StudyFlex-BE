package com.umc.StudyFlexBE.controller;

import com.umc.StudyFlexBE.dto.response.BaseResponse;
import com.umc.StudyFlexBE.dto.response.BaseResponseStatus;
import com.umc.StudyFlexBE.entity.Category;
import com.umc.StudyFlexBE.entity.Study;
import com.umc.StudyFlexBE.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CERTIFIED','ROLE_USER')")
@RequestMapping("/app/category")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {

        this.categoryService = categoryService;

    }

    @GetMapping()
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }


    @GetMapping("/{categoryName}/study")
    public ResponseEntity<BaseResponse<?>> getStudyByCategory(@PathVariable String categoryName) {
        try {
            Study study = categoryService.getStudyByCategoryName(categoryName);
            if (study == null) {
                BaseResponse<Object> response = new BaseResponse<>(BaseResponseStatus.NO_SUCH_STUDY, null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            } else {
                Map<String, Object> studyData = new HashMap<>();
                studyData.put("studyId", study.getId());
                studyData.put("studyName", study.getName());
                String studyPageUrl = "https://example.com/app/studies/" + study.getId();
                studyData.put("studyPageUrl", studyPageUrl);
                BaseResponse<Map<String, Object>> response = new BaseResponse<>(BaseResponseStatus.SUCCESS, studyData);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            BaseResponse<Object> response = new BaseResponse<>(BaseResponseStatus.INTERNAL_SERVER_ERROR, null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
