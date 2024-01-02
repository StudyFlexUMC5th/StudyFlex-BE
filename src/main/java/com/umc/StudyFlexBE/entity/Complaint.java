package com.umc.StudyFlexBE.entity;


import java.sql.Timestamp;

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

    @Column(nullable = false)
    private Long complaintMemberId;

    @Column(length = 100)
    private String complaintedMember;

    @Enumerated(EnumType.STRING)
    private ComplaintCategory complaintCategory;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;


}
