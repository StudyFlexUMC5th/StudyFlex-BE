package com.umc.StudyFlexBE.dto.request;

public class RankingDTO {
        private Long studyId;
        private String studyName;
        private double score;

        // 기본 생성자
        public RankingDTO() {
        }

        // 모든 필드를 포함하는 생성자
        public RankingDTO(Long studyId, String studyName, double score) {
            this.studyId = studyId;
            this.studyName = studyName;
            this.score = score;
        }

        // 게터 메서드
        public Long getStudyId() {
            return studyId;
        }

        public String getStudyName() {
            return studyName;
        }

        public double getScore() {
            return score;
        }

        // 세터 메서드
        public void setStudyId(Long studyId) {
            this.studyId = studyId;
        }

        public void setStudyName(String studyName) {
            this.studyName = studyName;
        }

        public void setScore(double score) {
            this.score = score;
        }
    }

