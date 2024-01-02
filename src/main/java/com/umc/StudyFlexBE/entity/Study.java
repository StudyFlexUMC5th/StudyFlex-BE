package com.umc.StudyFlexBE.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Study {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_id")
    private Long id;

    @Column(name = "study_name", length = 100)
    private String name;


    @OneToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(name = "study_status", columnDefinition = "ENUM('모집중', '모집완료')")
    private String status;

    @Column(name = "thumbnail_url", length = 2083)
    private String thumbnailUrl;

    @Column(name = "study_created_at", updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "study_updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(name = "study_completed_at")
    private LocalDateTime completedAt;

    @Column(name = "leader_id")
    private Long leaderId;

    @Column(name = "max_members")
    private Integer maxMembers;

    @Column(name = "current_members")
    private Integer currentMembers;

    @Column(name = "study_hits")
    private BigInteger hits;

}

