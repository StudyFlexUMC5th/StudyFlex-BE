package com.umc.StudyFlexBE.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Member {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long member_id;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "web_email", length = 100)
    private String web_email;

    @Column(name = "role")
    private Role role;

    @Column(name = "member_type")
    private MemberType member_type;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "school",length = 100)
    private String school;


    @BatchSize(size = 100)
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<StudyParticipation> studyParticipationList = new ArrayList<>();

    public List<String> getRoles() {
        return Collections.singletonList(role.name());
}

    @Transient
    private boolean isNewUser = false;

}
