package com.umc.StudyFlexBE.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CheckAuthCodeDto {
    @JsonProperty
    @NotNull
    private String webEmail;

    @JsonProperty
    @NotNull
    private String univName;

    @JsonProperty
    @NotNull
    private int code;
}
