package com.umc.StudyFlexBE.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Completed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "progress_id")
    private Progress progress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_participation_id")
    private StudyParticipation studyParticipation;
}
