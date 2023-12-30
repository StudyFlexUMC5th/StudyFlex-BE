package com.umc.StudyFlexBE.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@NoArgsConstructor
public class SignUpDto {

    @JsonProperty
    @NotNull
    private String email;

    @JsonProperty
    @NotNull
    private String password;






}
