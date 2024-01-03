package com.umc.StudyFlexBE.service;

import com.umc.StudyFlexBE.entity.Study;
import com.umc.StudyFlexBE.repository.StudyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchService {
    private final StudyRepository studyRepository;

    @Autowired
    public SearchService(StudyRepository studyRepository) {

        this.studyRepository = studyRepository;
    }

    public Study searchStudies(String query) {
        return studyRepository
                .findByNameContaining(query).stream().findFirst().orElse(null);
    }


}
