package com.umc.StudyFlexBE.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complaint_member_id", referencedColumnName = "id", nullable = false)
    private Member complaint_member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complainted_member_id", referencedColumnName = "id", nullable = false)
    private Member complainted_member;

    @Enumerated(EnumType.STRING)
    @Column(name = "complaint_category", nullable = false)
    private ComplaintCategory complaint_category;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

}
