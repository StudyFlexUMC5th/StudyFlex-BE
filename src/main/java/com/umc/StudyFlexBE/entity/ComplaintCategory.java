package com.umc.StudyFlexBE.entity;

public enum ComplaintCategory {
    SPAM_AD("스팸 홍보/도배글입니다."),
    OBSCENE("음란물입니다."),
    ILLEGAL_INFO("불법정보를 포함하고 있습니다."),
    HARMFUL_YOUTH("청소년에게 유해한 내용입니다."),
    ABUSE("욕설/생명경시/혐오/차별적 표현입니다."),
    PRIVACY_VIOLATION("개인정보 노출 게시물입니다."),
    UNPLEASANT_EXPRESSION("불쾌한 표현이 있습니다."),
    ETC("기타");

    private final String description;

    ComplaintCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
