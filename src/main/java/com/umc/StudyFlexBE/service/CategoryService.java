package com.umc.StudyFlexBE.service;

import com.umc.StudyFlexBE.entity.Category;
import com.umc.StudyFlexBE.entity.Study;
import com.umc.StudyFlexBE.repository.CategoryRepository;
import com.umc.StudyFlexBE.repository.StudyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final StudyRepository studyRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, StudyRepository studyRepository) {
        this.categoryRepository = categoryRepository;
        this.studyRepository = studyRepository;
    }

    public Study getStudyByCategoryName(String categoryName) {
        // 카테고리 이름을 기반으로 해당 카테고리를 검색
        Optional<Category> categoryOptional = categoryRepository.findByName(categoryName);

        if (categoryOptional.isPresent()) {
            // 카테고리가 존재할 경우 해당 카테고리의 스터디를 반환
            Category category = categoryOptional.get();
            return studyRepository.findByCategory(category)
                    .orElse(null);
            // 해당 카테고리에 스터디가 없을 경우 null 반환
        } else {

            // 카테고리가 존재하지 않을 경우 null 반환
            return null;
        }
    }
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
