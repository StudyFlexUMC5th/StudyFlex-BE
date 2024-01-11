package com.umc.StudyFlexBE.entity;

import java.sql.Timestamp;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Inquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member_id;

    @Column(name = "title",length = 100, nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "is_opened", nullable = false)
    private Boolean is_opened = true;

    @Column(name = "is_answered", nullable = false)
    private Boolean is_answered = false;

    @Column(name = "view", nullable = false)
    private Integer view = 0;

    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private Timestamp created_at;

    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    private Timestamp updated_at;

    @OneToOne(mappedBy = "inquiry", cascade = CascadeType.ALL)
    private InquiryAnswer inquiryAnswer;

    public boolean getIs_answered() {
        return is_answered;
    }
}
