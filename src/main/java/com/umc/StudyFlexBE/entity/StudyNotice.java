package com.umc.StudyFlexBE.entity;

import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StudyNotice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 15)
    private String title;

    @Column(name = "content", length = 1000)
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "view")
    private int view;

    @ManyToOne
    @JoinColumn(name = "study_id")
    private Study study;

}
